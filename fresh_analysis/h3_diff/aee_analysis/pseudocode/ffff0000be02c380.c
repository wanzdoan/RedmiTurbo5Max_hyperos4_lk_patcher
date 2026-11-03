
uint FUN_ffff0000be02c380(long param_1,int param_2)

{
  long lVar1;
  uint uVar2;
  int iVar3;
  long lVar4;
  undefined4 uVar5;
  uint uVar6;
  ulong uVar7;
  
  lVar4 = FUN_ffff0000be02e184(0xdb45);
  if (lVar4 == 0) {
    uVar6 = 0;
    uVar5 = 0;
  }
  else {
    uVar6 = *(uint *)(lVar4 + 8);
    uVar5 = *(undefined4 *)(lVar4 + 0xc);
  }
  uVar2 = FUN_ffff0000be0611dc(param_1,param_2,s_DFD_triggered_Please_refer_to_df_ffff0000be0b1e69);
  iVar3 = FUN_ffff0000be0611dc
                    (param_1 + (ulong)uVar2,param_2 - uVar2,
                     s_plat_sram_flag1___0x_x___0__dfd__ffff0000be0b1eaf,uVar6,uVar6 & 1);
  uVar2 = iVar3 + uVar2;
  iVar3 = FUN_ffff0000be0611dc
                    (param_1 + (ulong)uVar2,param_2 - uVar2,
                     s_plat_sram_flag2___0x_x__base_add_ffff0000be0b9cfb,uVar5);
  uVar2 = uVar2 + iVar3;
  if (DAT_ffff0000be118400 != 0) {
    if ((DAT_ffff0000be1183d8 != 0) && (DAT_ffff0000be118380 != 0)) {
      lVar4 = 0;
      uVar7 = 0;
      do {
        lVar1 = DAT_ffff0000be118400 + lVar4;
        iVar3 = FUN_ffff0000be0611dc
                          (param_1 + (ulong)uVar2,param_2 - uVar2,
                           s__LAST_PC__CORE__d_PC___0x_016llx_ffff0000be0ad806,uVar7 & 0xffffffff,
                           *(undefined8 *)(lVar1 + 8),*(undefined8 *)(lVar1 + 0x18),
                           *(undefined8 *)(lVar1 + 0x20));
        uVar2 = iVar3 + uVar2;
        uVar7 = uVar7 + 1;
        lVar4 = lVar4 + 0x38;
      } while (uVar7 < DAT_ffff0000be118380);
    }
    thunk_FUN_ffff0000be09937c(DAT_ffff0000be118400);
  }
  return uVar2;
}


