// ffff000090726818  size=1140  FUN_ffff000090726818

/* WARNING: Removing unreachable block (ram,0xffff000090726c5c) */
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090726818(code *param_1)

{
  long lVar1;
  undefined8 uVar2;
  int iVar3;
  long lVar4;
  int iVar5;
  uint *puVar6;
  int iVar7;
  uint uVar8;
  int *piVar9;
  long lVar10;
  long lVar11;
  int iVar12;
  ulong uVar13;
  uint uVar14;
  ulong uVar15;
  undefined1 local_80 [16];
  code *local_70;
  undefined4 local_64;
  
  piVar9 = (int *)local_80;
  lVar1 = FUN_ffff00009077d24c(0x4000);
  if (lVar1 == 0) {
    return;
  }
  if (_DAT_ffff00009086d390 == 0) {
    iVar5 = 0;
    goto LAB_ffff000090726bf0;
  }
  iVar5 = 0x18;
  local_64 = 0;
  local_70 = param_1;
  FUN_ffff00009077e858(lVar1,_DAT_ffff00009086d390,0x18);
  iVar7 = 0;
  lVar10 = _DAT_ffff00009086d390;
  do {
    if (*(int *)(lVar10 + 0xc) != 0) {
      *(int **)(piVar9 + 2) = piVar9;
      uVar15 = 0;
      uVar8 = iVar7 << 9;
      *piVar9 = iVar7;
      piVar9[1] = uVar8;
      do {
        lVar10 = *(long *)(*(long *)(lVar10 + 0x18) + uVar15 * 8);
        *(uint *)(lVar10 + 0x88) = *(uint *)(lVar10 + 0x88) & 0xfffff9ff | uVar8;
        local_64 = *(undefined4 *)(lVar10 + 0x88);
        iVar7 = iVar5;
        if ((long)iVar5 - 0x3ffcU < 0xffffffffffffc000) {
LAB_ffff000090726be0:
          piVar9 = *(int **)(piVar9 + 2);
          goto LAB_ffff000090726bec;
        }
        FUN_ffff00009077e858(lVar1 + iVar5,&local_64,4);
        uVar14 = 0;
        uVar8 = 0;
        do {
          *(uint *)(lVar10 + 0x88) =
               uVar14 & 0x7ffff800 | uVar8 & 0x1f8 | *(uint *)(lVar10 + 0x88) & 0xfffff607;
          lVar11 = *(long *)(*(long *)(_DAT_ffff00009086d390 + 0x18) + uVar15 * 8);
          iVar7 = iVar5 + uVar8 + 4;
          local_64 = *(undefined4 *)(lVar11 + 0xa00);
          if ((long)iVar7 - 0x3ffcU < 0xffffffffffffc000) goto LAB_ffff000090726be0;
          FUN_ffff00009077e858(lVar1 + iVar7,&local_64,4);
          lVar4 = (long)(int)(iVar5 + uVar8 + 8);
          local_64 = *(undefined4 *)(lVar11 + 0xa04);
          if (lVar4 - 0x3ffcU < 0xffffffffffffc000) {
            iVar7 = iVar5 + uVar8 + 8;
            goto LAB_ffff000090726be0;
          }
          FUN_ffff00009077e858(lVar1 + lVar4,&local_64,4);
          uVar8 = uVar8 + 8;
          uVar14 = uVar14 + 0x20;
        } while (uVar8 != 0x408);
        uVar15 = uVar15 + 1;
        iVar5 = iVar5 + 0x40c;
        uVar8 = piVar9[1];
        lVar10 = _DAT_ffff00009086d390;
      } while (uVar15 < *(uint *)(_DAT_ffff00009086d390 + 0xc));
      iVar7 = *piVar9;
      piVar9 = *(int **)(piVar9 + 2);
    }
    iVar7 = iVar7 + 1;
  } while (iVar7 != 3);
  if (*(int *)(lVar10 + 0xc) != 0) {
    uVar15 = 0;
    do {
      lVar10 = *(long *)(*(long *)(lVar10 + 0x18) + uVar15 * 8);
      local_64 = *(undefined4 *)(lVar10 + 0xc00);
      iVar7 = iVar5;
      if ((long)iVar5 - 0x3ffcU < 0xffffffffffffc000) goto LAB_ffff000090726bec;
      FUN_ffff00009077e858(lVar1 + iVar5,&local_64,4);
      iVar7 = iVar5 + 4;
      local_64 = *(undefined4 *)
                  (*(long *)(*(long *)(_DAT_ffff00009086d390 + 0x18) + uVar15 * 8) + 0xd80);
      if ((long)iVar7 - 0x3ffcU < 0xffffffffffffc000) goto LAB_ffff000090726bec;
      FUN_ffff00009077e858(lVar1 + iVar7,&local_64,4);
      iVar7 = 0;
      iVar5 = iVar5 + 8;
      do {
        iVar3 = iVar5;
        uVar13 = 0;
        puVar6 = *(uint **)(*(long *)(_DAT_ffff00009086d390 + 0x18) + uVar15 * 8);
        *puVar6 = *puVar6 & 0xfe1fffff | iVar7 << 0x15;
        do {
          lVar11 = (long)(iVar3 + (int)uVar13);
          local_64 = *(undefined4 *)(lVar10 + 0xc04 + uVar13);
          if (lVar11 - 0x3ffcU < 0xffffffffffffc000) goto LAB_ffff000090726c4c;
          FUN_ffff00009077e858(lVar1 + lVar11,&local_64,4);
          uVar13 = uVar13 + 4;
        } while (uVar13 < 0x9d);
        lVar11 = *(long *)(*(long *)(_DAT_ffff00009086d390 + 0x18) + uVar15 * 8);
        iVar12 = (int)uVar13;
        local_64 = *(undefined4 *)(lVar11 + 0xd70);
        if ((long)(iVar3 + iVar12) - 0x3ffcU < 0xffffffffffffc000) {
LAB_ffff000090726c4c:
          iVar7 = iVar3 + (int)uVar13;
          goto LAB_ffff000090726bec;
        }
        FUN_ffff00009077e858(lVar1 + (iVar3 + iVar12),&local_64,4);
        lVar4 = (long)(iVar3 + iVar12 + 4);
        local_64 = *(undefined4 *)(lVar11 + 0xd74);
        if (lVar4 - 0x3ffcU < 0xffffffffffffc000) {
          iVar7 = iVar3 + iVar12 + 4;
          goto LAB_ffff000090726bec;
        }
        FUN_ffff00009077e858(lVar1 + lVar4,&local_64,4);
        lVar4 = (long)(iVar3 + iVar12 + 8);
        local_64 = *(undefined4 *)(lVar11 + 0xd78);
        if (lVar4 - 0x3ffcU < 0xffffffffffffc000) {
          iVar7 = iVar3 + iVar12 + 8;
          goto LAB_ffff000090726bec;
        }
        FUN_ffff00009077e858(lVar1 + lVar4,&local_64,4);
        lVar4 = (long)(iVar3 + iVar12 + 0xc);
        local_64 = *(undefined4 *)(lVar11 + 0xd7c);
        if (lVar4 - 0x3ffcU < 0xffffffffffffc000) {
          iVar7 = iVar3 + iVar12 + 0xc;
          goto LAB_ffff000090726bec;
        }
        FUN_ffff00009077e858(lVar1 + lVar4,&local_64,4);
        iVar7 = iVar7 + 1;
        iVar5 = iVar3 + iVar12 + 0x10;
      } while (iVar7 != 10);
      uVar15 = uVar15 + 1;
      iVar5 = iVar3 + iVar12 + 0x10;
      lVar10 = _DAT_ffff00009086d390;
    } while (uVar15 < *(uint *)(_DAT_ffff00009086d390 + 0xc));
    iVar5 = iVar3 + iVar12 + 0x10;
  }
  param_1 = *(code **)((long)piVar9 + 0x10);
  if (0x4000 < iVar5) {
    iVar7 = 0;
    goto LAB_ffff000090726bf8;
  }
LAB_ffff000090726c08:
  if (0 < iVar5) {
    uVar2 = FUN_ffff00009077b06c(lVar1);
    (*param_1)(uVar2,iVar5);
  }
LAB_ffff000090726c20:
  thunk_FUN_ffff0000907c2ad8(lVar1);
  return;
LAB_ffff000090726bec:
  iVar5 = iVar7;
  param_1 = *(code **)((long)piVar9 + 0x10);
LAB_ffff000090726bf0:
  iVar7 = -1;
LAB_ffff000090726bf8:
  if (0x3fff < iVar5) {
    iVar5 = 0x4000;
  }
  if (iVar7 == 0) goto LAB_ffff000090726c20;
  goto LAB_ffff000090726c08;
}


