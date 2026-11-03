// ffff000090774b04  size=1324  FUN_ffff000090774b04

undefined4 FUN_ffff000090774b04(long *param_1,int param_2)

{
  uint uVar1;
  ulong uVar2;
  char *pcVar3;
  char *pcVar4;
  long lVar5;
  long lVar6;
  int iVar7;
  byte bVar8;
  short sVar9;
  char cVar10;
  undefined *puVar11;
  undefined *puVar12;
  ulong uVar13;
  uint uVar14;
  int iVar15;
  long lVar16;
  long lVar17;
  
  puVar12 = PTR_DAT_ffff000090839a18;
  puVar11 = PTR_DAT_ffff000090839a10;
LAB_ffff000090774b4c:
  do {
    uVar14 = *(uint *)((long)param_1 + 0xb4);
    if (uVar14 < 0x103) {
      FUN_ffff000090772d90(param_1);
      uVar14 = *(uint *)((long)param_1 + 0xb4);
      if ((param_2 == 0) && (uVar14 < 0x103)) {
        return 0;
      }
      if (uVar14 == 0) {
        *(undefined4 *)((long)param_1 + 0x172c) = 0;
        if (param_2 == 4) {
          uVar13 = param_1[0x13];
          if ((long)uVar13 < 0) {
            lVar16 = 0;
          }
          else {
            lVar16 = param_1[0xc] + (uVar13 & 0xffffffff);
          }
          FUN_ffff000090779150(param_1,lVar16,*(uint *)((long)param_1 + 0xac) - uVar13,1);
          lVar17 = *param_1;
          lVar16 = *(long *)(lVar17 + 0x38);
          param_1[0x13] = (ulong)*(uint *)((long)param_1 + 0xac);
          FUN_ffff000090778f5c(lVar16);
          uVar14 = *(uint *)(lVar17 + 0x20);
          if (*(uint *)(lVar16 + 0x28) <= *(uint *)(lVar17 + 0x20)) {
            uVar14 = *(uint *)(lVar16 + 0x28);
          }
          uVar13 = (ulong)uVar14;
          if (uVar14 != 0) {
            FUN_ffff00009077eb20
                      (*(undefined8 *)(lVar17 + 0x18),*(undefined8 *)(lVar16 + 0x20),uVar13);
            lVar5 = *(long *)(lVar16 + 0x20);
            lVar6 = *(long *)(lVar16 + 0x28);
            *(ulong *)(lVar17 + 0x18) = *(long *)(lVar17 + 0x18) + uVar13;
            lVar6 = lVar6 - uVar13;
            *(ulong *)(lVar17 + 0x28) = *(long *)(lVar17 + 0x28) + uVar13;
            *(uint *)(lVar17 + 0x20) = *(int *)(lVar17 + 0x20) - uVar14;
            *(ulong *)(lVar16 + 0x20) = lVar5 + uVar13;
            *(long *)(lVar16 + 0x28) = lVar6;
            if (lVar6 == 0) {
              *(undefined8 *)(lVar16 + 0x20) = *(undefined8 *)(lVar16 + 0x10);
            }
          }
          if (*(int *)(*param_1 + 0x20) != 0) {
            return 3;
          }
          return 2;
        }
        if (*(int *)((long)param_1 + 0x170c) != 0) {
          uVar13 = param_1[0x13];
          if ((long)uVar13 < 0) {
            lVar16 = 0;
          }
          else {
            lVar16 = param_1[0xc] + (uVar13 & 0xffffffff);
          }
          FUN_ffff000090779150(param_1,lVar16,*(uint *)((long)param_1 + 0xac) - uVar13,0);
          lVar17 = *param_1;
          lVar16 = *(long *)(lVar17 + 0x38);
          param_1[0x13] = (ulong)*(uint *)((long)param_1 + 0xac);
          FUN_ffff000090778f5c(lVar16);
          uVar14 = *(uint *)(lVar17 + 0x20);
          if (*(uint *)(lVar16 + 0x28) <= *(uint *)(lVar17 + 0x20)) {
            uVar14 = *(uint *)(lVar16 + 0x28);
          }
          uVar13 = (ulong)uVar14;
          if (uVar14 != 0) {
            FUN_ffff00009077eb20
                      (*(undefined8 *)(lVar17 + 0x18),*(undefined8 *)(lVar16 + 0x20),uVar13);
            lVar5 = *(long *)(lVar16 + 0x20);
            lVar6 = *(long *)(lVar16 + 0x28);
            *(ulong *)(lVar17 + 0x18) = *(long *)(lVar17 + 0x18) + uVar13;
            lVar6 = lVar6 - uVar13;
            *(ulong *)(lVar17 + 0x28) = *(long *)(lVar17 + 0x28) + uVar13;
            *(uint *)(lVar17 + 0x20) = *(int *)(lVar17 + 0x20) - uVar14;
            *(ulong *)(lVar16 + 0x20) = lVar5 + uVar13;
            *(long *)(lVar16 + 0x28) = lVar6;
            if (lVar6 == 0) {
              *(undefined8 *)(lVar16 + 0x20) = *(undefined8 *)(lVar16 + 0x10);
            }
          }
          if (*(int *)(*param_1 + 0x20) == 0) {
            return 0;
          }
        }
        return 1;
      }
      *(undefined4 *)(param_1 + 0x14) = 0;
      if (2 < uVar14) goto LAB_ffff000090774b8c;
      uVar13 = (ulong)*(uint *)((long)param_1 + 0xac);
LAB_ffff000090774c60:
      uVar14 = *(uint *)((long)param_1 + 0x170c);
      bVar8 = *(byte *)(param_1[0xc] + uVar13);
      *(uint *)((long)param_1 + 0x170c) = uVar14 + 1;
      *(undefined1 *)(param_1[0x2e0] + (ulong)uVar14) = 0;
      uVar14 = *(uint *)((long)param_1 + 0x170c);
      *(uint *)((long)param_1 + 0x170c) = uVar14 + 1;
      *(undefined1 *)(param_1[0x2e0] + (ulong)uVar14) = 0;
      uVar14 = *(uint *)((long)param_1 + 0x170c);
      *(uint *)((long)param_1 + 0x170c) = uVar14 + 1;
      *(byte *)(param_1[0x2e0] + (ulong)uVar14) = bVar8;
      iVar7 = *(int *)((long)param_1 + 0xb4);
      iVar15 = *(int *)((long)param_1 + 0x170c);
      lVar16 = param_1[0x2e2];
      uVar14 = *(int *)((long)param_1 + 0xac) + 1;
      *(short *)((long)param_1 + (ulong)bVar8 * 4 + 0xd4) =
           *(short *)((long)param_1 + (ulong)bVar8 * 4 + 0xd4) + 1;
      *(int *)((long)param_1 + 0xb4) = iVar7 + -1;
      *(uint *)((long)param_1 + 0xac) = uVar14;
      if (iVar15 != (int)lVar16) goto LAB_ffff000090774b4c;
    }
    else {
      *(undefined4 *)(param_1 + 0x14) = 0;
LAB_ffff000090774b8c:
      uVar13 = (ulong)*(uint *)((long)param_1 + 0xac);
      if (*(uint *)((long)param_1 + 0xac) == 0) {
        uVar13 = 0;
        goto LAB_ffff000090774c60;
      }
      pcVar3 = (char *)(param_1[0xc] + uVar13);
      cVar10 = pcVar3[-1];
      if (((cVar10 != *pcVar3) || (cVar10 != pcVar3[1])) || (cVar10 != pcVar3[2]))
      goto LAB_ffff000090774c60;
      iVar7 = (int)pcVar3;
      lVar16 = 0;
      do {
        pcVar4 = pcVar3 + lVar16;
        if (cVar10 != pcVar4[3]) {
          iVar15 = (int)pcVar4 + 3;
          goto LAB_ffff000090774dc8;
        }
        iVar15 = (int)lVar16;
        if (cVar10 != pcVar4[4]) {
          iVar15 = iVar7 + iVar15 + 4;
          goto LAB_ffff000090774dc8;
        }
        pcVar4 = pcVar3 + lVar16;
        if (cVar10 != pcVar4[5]) {
          iVar15 = (int)pcVar4 + 5;
          goto LAB_ffff000090774dc8;
        }
        if (cVar10 != pcVar4[6]) {
          iVar15 = iVar7 + iVar15 + 6;
          goto LAB_ffff000090774dc8;
        }
        pcVar4 = pcVar3 + lVar16;
        if (cVar10 != pcVar4[7]) {
          iVar15 = (int)pcVar4 + 7;
          goto LAB_ffff000090774dc8;
        }
        if (cVar10 != pcVar4[8]) {
          iVar15 = iVar7 + iVar15 + 8;
          goto LAB_ffff000090774dc8;
        }
        pcVar4 = pcVar3 + lVar16;
        if (cVar10 != pcVar4[9]) {
          iVar15 = (int)pcVar4 + 9;
          goto LAB_ffff000090774dc8;
        }
        lVar17 = lVar16 + 8;
      } while ((cVar10 == pcVar4[10]) && (uVar2 = lVar16 + 2, lVar16 = lVar17, uVar2 < 0xfa));
      iVar15 = iVar7 + (int)lVar17 + 2;
LAB_ffff000090774dc8:
      uVar1 = (iVar15 - (iVar7 + 0x102)) + 0x102;
      if (uVar14 <= uVar1) {
        uVar1 = uVar14;
      }
      *(uint *)(param_1 + 0x14) = uVar1;
      if (uVar1 < 3) goto LAB_ffff000090774c60;
      uVar14 = *(uint *)((long)param_1 + 0x170c);
      *(uint *)((long)param_1 + 0x170c) = uVar14 + 1;
      *(undefined1 *)(param_1[0x2e0] + (ulong)uVar14) = 1;
      uVar14 = *(uint *)((long)param_1 + 0x170c);
      *(uint *)((long)param_1 + 0x170c) = uVar14 + 1;
      *(undefined1 *)(param_1[0x2e0] + (ulong)uVar14) = 0;
      uVar14 = *(uint *)((long)param_1 + 0x170c);
      *(uint *)((long)param_1 + 0x170c) = uVar14 + 1;
      *(char *)(param_1[0x2e0] + (ulong)uVar14) = (char)(uVar1 - 3);
      iVar7 = *(int *)((long)param_1 + 0xac);
      iVar15 = *(int *)((long)param_1 + 0x170c);
      *(short *)((long)param_1 + (ulong)(byte)puVar11[(ulong)(uVar1 - 3) & 0xff] * 4 + 0x4d8) =
           *(short *)((long)param_1 + (ulong)(byte)puVar11[(ulong)(uVar1 - 3) & 0xff] * 4 + 0x4d8) +
           1;
      lVar16 = param_1[0x14];
      bVar8 = *puVar12;
      *(undefined4 *)(param_1 + 0x14) = 0;
      sVar9 = *(short *)((long)param_1 + (ulong)bVar8 * 4 + 0x9c8);
      uVar14 = iVar7 + (int)lVar16;
      lVar17 = param_1[0x2e2];
      *(int *)((long)param_1 + 0xb4) = *(int *)((long)param_1 + 0xb4) - (int)lVar16;
      *(uint *)((long)param_1 + 0xac) = uVar14;
      *(short *)((long)param_1 + (ulong)bVar8 * 4 + 0x9c8) = sVar9 + 1;
      if (iVar15 != (int)lVar17) goto LAB_ffff000090774b4c;
    }
    uVar13 = param_1[0x13];
    if ((long)uVar13 < 0) {
      lVar16 = 0;
    }
    else {
      lVar16 = param_1[0xc] + (uVar13 & 0xffffffff);
    }
    FUN_ffff000090779150(param_1,lVar16,uVar14 - uVar13,0);
    lVar17 = *param_1;
    lVar16 = *(long *)(lVar17 + 0x38);
    param_1[0x13] = (ulong)*(uint *)((long)param_1 + 0xac);
    FUN_ffff000090778f5c(lVar16);
    uVar14 = *(uint *)(lVar17 + 0x20);
    if (*(uint *)(lVar16 + 0x28) <= *(uint *)(lVar17 + 0x20)) {
      uVar14 = *(uint *)(lVar16 + 0x28);
    }
    uVar13 = (ulong)uVar14;
    if (uVar14 != 0) {
      FUN_ffff00009077eb20(*(undefined8 *)(lVar17 + 0x18),*(undefined8 *)(lVar16 + 0x20),uVar13);
      lVar5 = *(long *)(lVar16 + 0x20);
      lVar6 = *(long *)(lVar16 + 0x28);
      *(ulong *)(lVar17 + 0x18) = *(long *)(lVar17 + 0x18) + uVar13;
      lVar6 = lVar6 - uVar13;
      *(ulong *)(lVar17 + 0x28) = *(long *)(lVar17 + 0x28) + uVar13;
      *(uint *)(lVar17 + 0x20) = *(int *)(lVar17 + 0x20) - uVar14;
      *(ulong *)(lVar16 + 0x20) = lVar5 + uVar13;
      *(long *)(lVar16 + 0x28) = lVar6;
      if (lVar6 == 0) {
        *(undefined8 *)(lVar16 + 0x20) = *(undefined8 *)(lVar16 + 0x10);
      }
    }
    if (*(int *)(*param_1 + 0x20) == 0) {
      return 0;
    }
  } while( true );
}


