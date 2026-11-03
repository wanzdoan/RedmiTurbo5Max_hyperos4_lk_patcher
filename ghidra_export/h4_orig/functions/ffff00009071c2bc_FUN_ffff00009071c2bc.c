// ffff00009071c2bc  size=316  FUN_ffff00009071c2bc

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009071c2bc(void)

{
  uint uVar1;
  long lVar2;
  uint uVar3;
  long lVar4;
  long *plVar5;
  uint uVar6;
  uint uVar7;
  int iVar8;
  long *plVar9;
  long *plVar10;
  long lVar11;
  long lVar12;
  
  if (1 < _DAT_ffff00009085acd0) {
    iVar8 = 0;
    uVar7 = 0;
    uVar3 = _DAT_ffff00009085acd0 - 1;
    uVar6 = _DAT_ffff00009085acd0;
    do {
      lVar4 = _DAT_ffff000090850c90;
      uVar1 = uVar7 + 1;
      lVar2 = _DAT_ffff000090850c90 + 8;
      plVar5 = (long *)(lVar2 + (ulong)uVar1 * 0x18);
      plVar9 = (long *)(lVar2 + (ulong)uVar7 * 0x18 + 8);
      lVar12 = *plVar9;
      plVar10 = plVar5 + 1;
      lVar11 = *plVar10;
      lVar2 = lVar12 + *(long *)(lVar2 + (ulong)uVar7 * 0x18);
      if (*plVar5 == lVar2) {
        if (uVar7 + 2 < uVar6) {
          *plVar10 = 0;
          *plVar9 = lVar11 + lVar12;
LAB_ffff00009071c3a4:
          FUN_ffff00009077ec38
                    (plVar5,lVar4 + (ulong)(uVar7 + 2) * 0x18 + 8,
                     (ulong)((iVar8 + uVar6) - 2) * 0x18);
          uVar3 = _DAT_ffff00009085acd0 - 1;
        }
        else {
          if (uVar7 + 2 != uVar6) goto LAB_ffff00009071c370;
          *plVar10 = 0;
          *plVar9 = lVar11 + lVar12;
        }
LAB_ffff00009071c3b0:
        uVar6 = uVar3;
        uVar1 = 0;
        _DAT_ffff00009085acd0 = uVar6;
      }
      else {
LAB_ffff00009071c370:
        if (*plVar5 != lVar2 && lVar11 == 0) {
          if (uVar7 + 2 < uVar6) goto LAB_ffff00009071c3a4;
          if (uVar7 + 2 == uVar6) goto LAB_ffff00009071c3b0;
        }
      }
      uVar7 = uVar1;
      if (uVar6 < 2) {
        return;
      }
      iVar8 = -uVar7;
      uVar3 = uVar6 - 1;
    } while (uVar7 < uVar3);
  }
  return;
}


