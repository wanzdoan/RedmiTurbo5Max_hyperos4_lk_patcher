// ffff00009078cb6c  size=228  FUN_ffff00009078cb6c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009078cb6c(void)

{
  undefined2 *puVar1;
  undefined4 *puVar2;
  long *plVar3;
  bool bVar4;
  undefined4 *puVar5;
  long *plVar6;
  uint uVar7;
  long lVar8;
  ulong uVar9;
  int iVar10;
  
  plVar3 = _DAT_ffff00009088a120;
  plVar6 = (long *)*_DAT_ffff00009088a120;
  if ((_DAT_ffff000016701100 >> 0x12 & 1) == 0) {
    if (plVar6 == (long *)0x0) {
      uVar7 = 0;
    }
    else {
      uVar7 = (int)plVar6[1] - *(uint *)(plVar6 + 6);
      if (*(uint *)((long)_DAT_ffff00009088a120 + 0x3c) <= uVar7) {
        uVar7 = *(uint *)((long)_DAT_ffff00009088a120 + 0x3c);
      }
      puVar2 = (undefined4 *)(*plVar6 + (ulong)*(uint *)(plVar6 + 6));
      uVar9 = (ulong)uVar7 & 0xfffc;
      lVar8 = (ulong)*(byte *)(_DAT_ffff00009088a120 + 8) * 0x10;
      if ((uint)uVar9 != 0) {
        iVar10 = -(uVar7 >> 2 & 0x3fff);
        puVar5 = puVar2;
        do {
          bVar4 = iVar10 != -1;
          iVar10 = iVar10 + 1;
          *(undefined4 *)(lVar8 + -0xffffe98fed00) = *puVar5;
          puVar5 = puVar5 + 1;
        } while (bVar4);
      }
      if ((uVar7 >> 1 & 1) != 0) {
        puVar1 = (undefined2 *)((long)puVar2 + uVar9);
        uVar9 = (ulong)((uint)uVar9 | 2);
        *(undefined2 *)(lVar8 + -0xffffe98fed00) = *puVar1;
      }
      if ((uVar7 & 1) != 0) {
        *(undefined1 *)(lVar8 + -0xffffe98fed00) = *(undefined1 *)((long)puVar2 + uVar9);
      }
      *(uint *)(plVar6 + 6) = (int)plVar6[6] + uVar7;
    }
    _DAT_ffff000016701100 = _DAT_ffff000016701100 | 0x40000;
    if ((uVar7 < *(uint *)((long)plVar3 + 0x3c)) || ((int)plVar6[1] == (int)plVar6[6])) {
      *(undefined4 *)(plVar6 + 6) = 0;
      _DAT_ffff00009088a030 = 3;
    }
  }
  return;
}


