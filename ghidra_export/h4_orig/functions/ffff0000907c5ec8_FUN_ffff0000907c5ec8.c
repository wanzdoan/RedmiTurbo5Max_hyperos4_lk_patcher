// ffff0000907c5ec8  size=996  FUN_ffff0000907c5ec8

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined4 FUN_ffff0000907c5ec8(long param_1,uint param_2,long param_3)

{
  char cVar1;
  bool bVar2;
  int iVar3;
  ulong uVar4;
  ulong uVar5;
  char *pcVar6;
  uint uVar7;
  uint uVar8;
  uint uVar9;
  uint uVar10;
  
  if (param_1 == 0) {
    return 0x108004;
  }
  if (param_3 == 0) {
    return 0x108001;
  }
  uVar4 = FUN_ffff00009077f250(param_3);
  if (param_2 < uVar4) {
    return 0x108001;
  }
  uVar5 = FUN_ffff00009077f250(param_1);
  if (uVar5 < uVar4) {
    return 0x108001;
  }
  uVar7 = 0;
  uVar8 = 0;
  iVar3 = 0;
  uVar9 = 0;
  bVar2 = true;
  do {
    cVar1 = *(char *)(param_1 + (ulong)uVar9);
    if (cVar1 == ' ' || param_2 == uVar9) {
      if (iVar3 == 2) {
        if (!bVar2) {
          if (0x80 < uVar9 - uVar7) {
            return 0x108003;
          }
          FUN_ffff00009077eb20
                    ((ulong)_DAT_ffff00009090e5f8 * 0xc2 + -0xffff6f6f25e7,param_1 + (ulong)uVar7);
          if (param_2 <= uVar9) goto LAB_ffff0000907c617c;
          pcVar6 = (char *)(param_1 + (ulong)uVar9);
          goto LAB_ffff0000907c60e8;
        }
      }
      else {
        if (iVar3 != 1) goto LAB_ffff0000907c5f78;
        uVar5 = (ulong)(uVar9 - uVar8);
        uVar4 = FUN_ffff00009077f250(param_3);
        if (uVar4 == uVar5) {
          iVar3 = FUN_ffff00009077eae8(param_1 + (ulong)uVar8,param_3,uVar5);
          if (iVar3 == 0) {
            if (0x40 < uVar9 - uVar8) {
              return 0x108001;
            }
            FUN_ffff00009077eb20
                      ((ulong)_DAT_ffff00009090e5f8 * 0xc2 + -0xffff6f6f2628,param_1 + (ulong)uVar8,
                       uVar5);
            if (param_2 <= uVar9) goto LAB_ffff0000907c6214;
            pcVar6 = (char *)(param_1 + (ulong)uVar9);
            goto LAB_ffff0000907c6144;
          }
        }
      }
LAB_ffff0000907c5f70:
      iVar3 = 0;
      bVar2 = true;
    }
    else if (cVar1 == '\"') {
      if (iVar3 != 2) {
        if (iVar3 != 3) {
          return 0x108005;
        }
        if (bVar2) goto LAB_ffff0000907c5f70;
        uVar10 = uVar9 + 1;
        if (0x80 < uVar10 - uVar7) {
          return 0x108003;
        }
        FUN_ffff00009077eb20
                  ((ulong)_DAT_ffff00009090e5f8 * 0xc2 + -0xffff6f6f25e7,param_1 + (ulong)uVar7);
        uVar7 = param_2;
        if (param_2 <= uVar10) {
          uVar7 = uVar10;
        }
        break;
      }
      iVar3 = 3;
      uVar7 = uVar9;
    }
    else if (cVar1 == '=') {
      if (iVar3 == 1) {
        uVar4 = FUN_ffff00009077f250(param_3);
        if (uVar4 == uVar9 - uVar8) {
          iVar3 = FUN_ffff00009077eae8(param_1 + (ulong)uVar8,param_3,uVar4);
          if (iVar3 != 0) goto LAB_ffff0000907c601c;
          if (0x40 < uVar9 - uVar8) {
            return 0x108003;
          }
          FUN_ffff00009077eb20
                    ((ulong)_DAT_ffff00009090e5f8 * 0xc2 + -0xffff6f6f2628,param_1 + (ulong)uVar8,
                     uVar4);
          bVar2 = false;
        }
        else {
LAB_ffff0000907c601c:
          bVar2 = true;
        }
        iVar3 = 2;
        uVar7 = uVar9 + 1;
      }
    }
    else if (iVar3 == 0) {
      if (0xf < _DAT_ffff00009090e5f8) {
        FUN_ffff00009077e11c(s__s_overflow_ffff0000907d8acf,s__cmdline__ffff0000907d5651);
        return 0x108006;
      }
      iVar3 = 1;
      uVar8 = uVar9;
    }
LAB_ffff0000907c5f78:
    uVar9 = uVar9 + 1;
    if (param_2 < uVar9) {
      return 0;
    }
  } while( true );
  while (uVar10 = uVar9, *(char *)(param_1 + (ulong)uVar9) == ' ') {
    uVar9 = uVar9 + 1;
    uVar10 = uVar7;
    if (param_2 <= uVar9) break;
  }
  if (uVar10 != param_2) {
    FUN_ffff00009077eb20(param_1 + (ulong)uVar8,param_1 + (ulong)uVar10,param_2 - uVar10);
  }
  FUN_ffff00009077ee50((param_1 + (ulong)uVar8 + (ulong)param_2) - (ulong)uVar10,0,uVar10 - uVar8);
  goto LAB_ffff0000907c6294;
  while (uVar9 = uVar9 + 1, pcVar6 = pcVar6 + 1, param_2 != uVar9) {
LAB_ffff0000907c60e8:
    if (*pcVar6 != ' ') goto LAB_ffff0000907c617c;
  }
  goto LAB_ffff0000907c615c;
LAB_ffff0000907c617c:
  param_2 = param_2 - uVar9;
  uVar4 = (ulong)param_2;
  uVar7 = uVar8;
  if (param_2 != 0) {
    do {
      uVar5 = (ulong)uVar9;
      uVar9 = uVar9 + 1;
      uVar4 = uVar4 - 1;
      *(undefined1 *)(param_1 + (ulong)uVar7) = *(undefined1 *)(param_1 + uVar5);
      *(undefined1 *)(param_1 + uVar5) = 0;
      uVar7 = uVar7 + 1;
    } while (uVar4 != 0);
  }
  goto LAB_ffff0000907c6244;
LAB_ffff0000907c6214:
  param_2 = param_2 - uVar9;
  uVar4 = (ulong)param_2;
  uVar7 = uVar8;
  if (param_2 != 0) {
    do {
      uVar5 = (ulong)uVar9;
      uVar9 = uVar9 + 1;
      uVar4 = uVar4 - 1;
      *(undefined1 *)(param_1 + (ulong)uVar7) = *(undefined1 *)(param_1 + uVar5);
      *(undefined1 *)(param_1 + uVar5) = 0;
      uVar7 = uVar7 + 1;
    } while (uVar4 != 0);
  }
  goto LAB_ffff0000907c6244;
  while (uVar9 = uVar9 + 1, pcVar6 = pcVar6 + 1, param_2 != uVar9) {
LAB_ffff0000907c6144:
    if (*pcVar6 != ' ') goto LAB_ffff0000907c6214;
  }
LAB_ffff0000907c615c:
  param_2 = 0;
LAB_ffff0000907c6244:
  *(undefined1 *)(param_1 + (ulong)(param_2 + uVar8)) = 0;
LAB_ffff0000907c6294:
  _DAT_ffff00009090e5f8 = _DAT_ffff00009090e5f8 + 1;
  return 0;
}


