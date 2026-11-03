// ffff00009073bb28  size=304  FUN_ffff00009073bb28

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

uint FUN_ffff00009073bb28(long param_1,int param_2)

{
  long lVar1;
  uint uVar2;
  int iVar3;
  long lVar4;
  undefined4 uVar5;
  uint uVar6;
  ulong uVar7;
  
  lVar4 = FUN_ffff00009073dc20(0xdb45);
  if (lVar4 == 0) {
    uVar6 = 0;
    uVar5 = 0;
  }
  else {
    uVar6 = *(uint *)(lVar4 + 8);
    uVar5 = *(undefined4 *)(lVar4 + 0xc);
  }
  uVar2 = FUN_ffff00009077f434(param_1,param_2,s_DFD_triggered_Please_refer_to_df_ffff0000907e49e8);
  iVar3 = FUN_ffff00009077f434
                    (param_1 + (ulong)uVar2,param_2 - uVar2,
                     s_plat_sram_flag1___0x_x___0__dfd__ffff0000907e4a2e,uVar6,uVar6 & 1);
  uVar2 = iVar3 + uVar2;
  iVar3 = FUN_ffff00009077f434
                    (param_1 + (ulong)uVar2,param_2 - uVar2,
                     s_plat_sram_flag2___0x_x__base_add_ffff0000907efa6b,uVar5);
  uVar2 = uVar2 + iVar3;
  if (_DAT_ffff00009086e0a0 != 0) {
    if ((_DAT_ffff00009086e078 != 0) && (_DAT_ffff00009086e020 != 0)) {
      lVar4 = 0;
      uVar7 = 0;
      do {
        lVar1 = _DAT_ffff00009086e0a0 + lVar4;
        iVar3 = FUN_ffff00009077f434
                          (param_1 + (ulong)uVar2,param_2 - uVar2,
                           s__LAST_PC__CORE__d_PC___0x_016llx_ffff0000907de209,uVar7 & 0xffffffff,
                           *(undefined8 *)(lVar1 + 8),*(undefined8 *)(lVar1 + 0x18),
                           *(undefined8 *)(lVar1 + 0x20));
        uVar2 = iVar3 + uVar2;
        uVar7 = uVar7 + 1;
        lVar4 = lVar4 + 0x38;
      } while (uVar7 < _DAT_ffff00009086e020);
    }
    thunk_FUN_ffff0000907c2ad8(_DAT_ffff00009086e0a0);
  }
  return uVar2;
}


