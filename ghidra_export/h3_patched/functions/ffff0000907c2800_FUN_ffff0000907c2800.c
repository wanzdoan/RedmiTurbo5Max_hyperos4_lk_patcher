// ffff0000907c2800  size=572  FUN_ffff0000907c2800

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

long * FUN_ffff0000907c2800(long param_1,uint param_2)

{
  ulong uVar1;
  long *plVar2;
  uint uVar3;
  ulong uVar4;
  long *plVar5;
  ulong uVar6;
  uint uVar7;
  long *plVar8;
  long lVar9;
  ulong uVar10;
  ulong uVar11;
  
  if ((param_2 & param_2 - 1) == 0) {
    uVar1 = param_1 + 0x10;
    if (uVar1 < 0x19) {
      uVar1 = 0x18;
    }
    uVar7 = param_2;
    if (param_2 < 0x11) {
      uVar7 = 0x10;
    }
    uVar3 = 0;
    if (param_2 != 0) {
      uVar3 = uVar7;
    }
    uVar11 = (ulong)uVar3;
    uVar1 = (uVar1 + 7 & 0xfffffffffffffff8) + uVar11;
    FUN_ffff0000907175f4(0xffff00009090d858,0xffffffff);
    plVar5 = _DAT_ffff00009090d898;
    if (_DAT_ffff00009090d898 != (long *)&DAT_ffff00009090d890) {
      do {
        plVar8 = (long *)plVar5[1];
        uVar6 = plVar5[2];
        if (uVar1 <= uVar6) goto LAB_ffff0000907c2960;
        plVar5 = plVar8;
      } while (plVar8 != (long *)&DAT_ffff00009090d890);
    }
    uVar6 = uVar1 + 0xfff;
    uVar10 = uVar6 & 0xfffffffffffff000;
    FUN_ffff00009071768c(0xffff00009090d858);
    uVar4 = FUN_ffff00009077d41c(uVar6 >> 0xc,0xffffffff);
    if (uVar4 == 0) {
      FUN_ffff00009077de54
                (s__s__d__failed_to_grow_kernel_hea_ffff0000907e3e55,s_heap_grow_ffff000090800020,
                 0x235,uVar10);
    }
    else {
      *(ulong *)(uVar4 + 0x10) = uVar10;
      FUN_ffff0000907c2afc();
      if ((uVar4 < _DAT_ffff00009090d838) || (_DAT_ffff00009090d838 == 0)) {
        _DAT_ffff00009090d838 = uVar4;
      }
      if (_DAT_ffff00009090d840 + _DAT_ffff00009090d838 < uVar10 + uVar4) {
        _DAT_ffff00009090d840 = (uVar10 + uVar4) - _DAT_ffff00009090d838;
      }
      if (-1 < (long)uVar6) {
        FUN_ffff0000907175f4(0xffff00009090d858,0xffffffff);
        plVar5 = _DAT_ffff00009090d898;
        if (_DAT_ffff00009090d898 != (long *)&DAT_ffff00009090d890) {
          do {
            plVar8 = (long *)plVar5[1];
            uVar6 = plVar5[2];
            if (uVar1 <= uVar6) {
LAB_ffff0000907c2960:
              *plVar8 = *plVar5;
              *(long **)(*plVar5 + 8) = plVar8;
              *plVar5 = 0;
              plVar5[1] = 0;
              if (uVar1 + 0x18 < uVar6) {
                plVar2 = (long *)((long)plVar5 + uVar1);
                *plVar2 = *plVar8;
                plVar2[2] = uVar6 - uVar1;
                lVar9 = *plVar8;
                plVar5[2] = uVar1;
                plVar2[1] = (long)plVar8;
                *(long **)(lVar9 + 8) = plVar2;
                *plVar8 = (long)plVar2;
                uVar6 = uVar1;
              }
              uVar1 = _DAT_ffff00009090d850;
              plVar8 = plVar5 + 2;
              if (param_2 != 0) {
                plVar8 = (long *)((uVar11 - 1) + (long)(plVar5 + 2) & -uVar11);
              }
              plVar8[-2] = (long)plVar5;
              plVar8[-1] = uVar6;
              _DAT_ffff00009090d848 = _DAT_ffff00009090d848 - uVar6;
              if (_DAT_ffff00009090d848 < uVar1) {
                _DAT_ffff00009090d850 = _DAT_ffff00009090d848;
              }
              FUN_ffff00009071768c(0xffff00009090d858);
              return plVar8;
            }
            plVar5 = plVar8;
          } while (plVar8 != (long *)&DAT_ffff00009090d890);
        }
        FUN_ffff00009071768c(0xffff00009090d858);
      }
    }
  }
  return (long *)0x0;
}


