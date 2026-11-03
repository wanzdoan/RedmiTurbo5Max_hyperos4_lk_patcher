
void FUN_ffff0000be00feb8(ulong param_1,ulong param_2)

{
  int *piVar1;
  ulong uVar2;
  long lVar3;
  ulong uVar4;
  long lVar5;
  long lVar6;
  ulong uVar7;
  
  if (DAT_ffff0000be0fb000 == 0) {
    FUN_ffff0000be063b40
              (s__s__mrdump_cb_is_NULL_ffff0000be0b089e,
               s_mrdump_mini_add_entry_ext64_ffff0000be0a698d);
    FUN_ffff0000be05fd98
              (s__s__mrdump_cb_is_NULL_ffff0000be0b089e,
               s_mrdump_mini_add_entry_ext64_ffff0000be0a698d);
    return;
  }
  lVar5 = 0;
  lVar6 = 0;
  uVar2 = 0xfff;
  if (*(long *)(DAT_ffff0000be0fb000 + 0x60) != 0x1000) {
    uVar2 = 0x3fff;
  }
  do {
    lVar3 = lVar5;
    if (DAT_ffff0000be0fd0c0 != 0x28) {
      lVar3 = lVar6;
    }
    piVar1 = (int *)(DAT_ffff0000be0fd0b8 + (ulong)DAT_ffff0000be0fd0c4 + lVar3);
    if (*piVar1 == 1) {
      uVar7 = *(ulong *)(piVar1 + 4);
      uVar4 = *(long *)(piVar1 + 8) + uVar7;
      if (uVar7 <= param_1 && param_2 <= uVar4) {
        return;
      }
      if ((uVar7 < param_2) && (param_1 < uVar4)) {
        if (param_1 <= uVar7) {
          uVar7 = param_1;
        }
        if (uVar4 <= param_2) {
          uVar4 = param_2;
        }
        goto LAB_ffff0000be00ff6c;
      }
    }
    else {
      uVar7 = param_1;
      uVar4 = param_2;
      if (*piVar1 == 0) {
LAB_ffff0000be00ff6c:
        uVar7 = uVar7 & (uVar2 ^ 0xffffffffffffffff);
        piVar1[0xc] = 0;
        piVar1[0xd] = 0;
        piVar1[0] = 1;
        piVar1[1] = 0;
        *(ulong *)(piVar1 + 4) = uVar7;
        piVar1[6] = 0;
        piVar1[7] = 0;
        *(long *)(piVar1 + 8) = (long)(int)(((uint)uVar2 - (int)uVar7) + (int)uVar4 & ~(uint)uVar2);
        piVar1[10] = 0;
        piVar1[0xb] = 0;
        return;
      }
    }
    lVar6 = lVar6 + 0x38;
    lVar5 = lVar5 + 0x20;
    if (lVar5 == 0x780) {
      FUN_ffff0000be063b40(s_mrdump__MINI_NR_SECTION_overflow_ffff0000be0b855b);
      FUN_ffff0000be05fd98(s_mrdump__MINI_NR_SECTION_overflow_ffff0000be0b855b);
      return;
    }
  } while( true );
}


