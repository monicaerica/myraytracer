import plyfile
import numpy as np

def read_ply(file_path):
    # Read PLY file
    ply_data = plyfile.PlyData.read(file_path)
    
    # Extract vertex coordinates
    vertices = np.vstack([ply_data['vertex']['x'], ply_data['vertex']['y'], ply_data['vertex']['z']]).T

    # Extract face indices (assuming triangles)
    faces = np.array(ply_data['face']['vertex_indices'])
    
    return vertices, faces

def convert_to_triangles(faces):
    triangles = []
    for face in faces:
        # Assuming faces are triangles
        if len(face) == 3:
            triangles.append(face)
        elif len(face) > 3:
            # Triangulate non-triangular faces
            triangles.extend([[face[0], face[i], face[i + 1]] for i in range(1, len(face) - 1)])
    
    return np.array(triangles)

def get_triangle_vertices(vertices, faces):
    triangle_vertices = []
    
    triangles = convert_to_triangles(faces)
    
    for triangle in triangles:
        triangle_coords = [vertices[index] for index in triangle]
        triangle_vertices.append(triangle_coords)
    
    return triangle_vertices

if __name__ == "__main__":
    # Replace 'your_file.ply' with the path to your PLY file
    ply_file_path = 'filename.ply'
    
    # Read PLY file
    vertices, faces = read_ply(ply_file_path)
    
    # Get triangle vertices
    triangle_vertices = get_triangle_vertices(vertices, faces)
    
    # Print the list of triangle vertices
    for i, triangle in enumerate(triangle_vertices, start=1):
        print(f"triangle(tri_mat, <<{triangle[0][0]},{triangle[0][1]},{triangle[0][2]}>, <{triangle[1][0]},{triangle[1][1]},{triangle[1][2]}>,<{triangle[2][0]},{triangle[2][1]},{triangle[2][2]}>>, scaling([1, 1, 1]))")


