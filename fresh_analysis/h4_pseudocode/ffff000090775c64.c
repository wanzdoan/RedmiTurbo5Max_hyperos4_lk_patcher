
uint FUN_ffff000090775c64(long param_1,ulong param_2)

{
  char *pcVar1;
  long lVar2;
  ulong uVar3;
  char *pcVar4;
  char *pcVar5;
  uint uVar6;
  uint uVar7;
  ushort uVar8;
  uint uVar9;
  int iVar10;
  uint uVar11;
  int iVar12;
  ulong uVar13;
  char cVar14;
  int iVar15;
  long lVar16;
  uint uVar17;
  char cVar18;
  
  uVar17 = *(uint *)(param_1 + 0xac);
  pcVar4 = (char *)(*(long *)(param_1 + 0x60) + (ulong)uVar17);
  uVar9 = *(int *)(param_1 + 0x50) - 0x106;
  uVar13 = (ulong)(int)*(uint *)(param_1 + 0xb8);
  uVar7 = *(uint *)(param_1 + 0xb4);
  uVar6 = 0;
  if (uVar9 <= uVar17) {
    uVar6 = uVar17 - uVar9;
  }
  uVar17 = *(uint *)(param_1 + 0xbc);
  if (*(uint *)(param_1 + 0xcc) <= *(uint *)(param_1 + 0xb8)) {
    uVar17 = *(uint *)(param_1 + 0xbc) >> 2;
  }
  iVar10 = (int)pcVar4;
  cVar18 = (pcVar4 + uVar13)[-1];
  uVar9 = *(uint *)(param_1 + 0xd0);
  if (uVar7 <= *(uint *)(param_1 + 0xd0)) {
    uVar9 = uVar7;
  }
  cVar14 = pcVar4[uVar13];
  do {
    iVar12 = (int)uVar13;
    pcVar1 = (char *)(*(long *)(param_1 + 0x60) + (param_2 & 0xffffffff));
    if ((((pcVar1[iVar12] == cVar14) && (pcVar1[(long)iVar12 + -1] == cVar18)) &&
        (*pcVar1 == *pcVar4)) && (pcVar1[1] == pcVar4[1])) {
      lVar16 = 0;
      do {
        pcVar5 = pcVar4 + lVar16;
        if (pcVar5[3] != pcVar1[lVar16 + 3]) {
          iVar15 = (int)pcVar5 + 3;
          goto LAB_ffff000090775e28;
        }
        iVar15 = (int)lVar16;
        if (pcVar5[4] != pcVar1[lVar16 + 4]) {
          iVar15 = iVar10 + iVar15 + 4;
          goto LAB_ffff000090775e28;
        }
        pcVar5 = pcVar4 + lVar16;
        if (pcVar5[5] != pcVar1[lVar16 + 5]) {
          iVar15 = (int)pcVar5 + 5;
          goto LAB_ffff000090775e28;
        }
        if (pcVar5[6] != pcVar1[lVar16 + 6]) {
          iVar15 = iVar10 + iVar15 + 6;
          goto LAB_ffff000090775e28;
        }
        pcVar5 = pcVar4 + lVar16;
        if (pcVar5[7] != pcVar1[lVar16 + 7]) {
          iVar15 = (int)pcVar5 + 7;
          goto LAB_ffff000090775e28;
        }
        if (pcVar5[8] != pcVar1[lVar16 + 8]) {
          iVar15 = iVar10 + iVar15 + 8;
          goto LAB_ffff000090775e28;
        }
        pcVar5 = pcVar4 + lVar16;
        if (pcVar5[9] != pcVar1[lVar16 + 9]) {
          iVar15 = (int)pcVar5 + 9;
          goto LAB_ffff000090775e28;
        }
        lVar2 = lVar16 + 8;
      } while ((pcVar5[10] == pcVar1[lVar16 + 10]) &&
              (uVar3 = lVar16 + 2, lVar16 = lVar2, uVar3 < 0xfa));
      iVar15 = iVar10 + (int)lVar2 + 2;
LAB_ffff000090775e28:
      iVar15 = iVar15 - (iVar10 + 0x102);
      uVar11 = iVar15 + 0x102;
      if (iVar12 < (int)uVar11) {
        *(int *)(param_1 + 0xb0) = (int)param_2;
        if ((int)uVar9 <= (int)uVar11) break;
        cVar14 = pcVar4[(int)uVar11];
        uVar13 = (ulong)uVar11;
        cVar18 = pcVar4[iVar15 + 0x101];
      }
    }
    uVar11 = (uint)uVar13;
    uVar17 = uVar17 - 1;
    uVar8 = *(ushort *)(*(long *)(param_1 + 0x70) + (param_2 & *(uint *)(param_1 + 0x58)) * 2);
    param_2 = (ulong)uVar8;
  } while (uVar6 < uVar8 && uVar17 != 0);
  if (uVar7 <= uVar11) {
    uVar11 = uVar7;
  }
  return uVar11;
}


