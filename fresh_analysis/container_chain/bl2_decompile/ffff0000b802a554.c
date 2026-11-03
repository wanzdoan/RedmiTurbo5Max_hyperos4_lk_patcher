
int FUN_ffff0000b802a554(uint *param_1)

{
  ulong uVar1;
  int iVar2;
  
  iVar2 = DAT_ffff0000b8124ff0 - DAT_ffff0000b8124fe8;
  FUN_ffff0000b804c33c
            (s__s__d___MMUP__mmup_reg_base_phy_a_ffff0000b808ab1f,s_mmup_do_regdump_ffff0000b808c833
             ,0x7d,0x31800000,iVar2);
  if (iVar2 - 0x4001U < 0xffffc000) {
    FUN_ffff0000b804c33c
              (s__MMUP__error___s_reg_size_0x_x___ffff0000b8089020,
               s_mmup_do_regdump_ffff0000b808c833,iVar2,0x4000);
    iVar2 = 0;
  }
  else {
    FUN_ffff0000b804d070(param_1,0,iVar2);
    DAT_ffff0000b80ea4b8 = DAT_ffff0000b80ea4b8 + 0x31800000;
    DAT_ffff0000b80ea4bc = DAT_ffff0000b80ea4bc + 0x31800000;
    DAT_ffff0000b80ea4c0 = DAT_ffff0000b80ea4c0 + 0x31800000;
    FUN_ffff0000b804c33c
              (s__s__d___MMUP__mmup_reg_save_list_ffff0000b809f8db,
               s_mmup_do_regdump_ffff0000b808c833,0x8a,0);
    uVar1 = (ulong)DAT_ffff0000b80ea4b8;
    *param_1 = DAT_ffff0000b80ea4b8;
    param_1[1] = 0x1e0;
    FUN_ffff0000b804cd40(param_1 + 2,uVar1 & 0xfffffff | 0xffff000030000000,0x1e0);
    FUN_ffff0000b804c33c
              (s__s__d___MMUP__mmup_reg_save_list_ffff0000b809f8db,
               s_mmup_do_regdump_ffff0000b808c833,0x8a,1,DAT_ffff0000b80ea4bc);
    uVar1 = (ulong)DAT_ffff0000b80ea4bc;
    param_1[0x7a] = DAT_ffff0000b80ea4bc;
    param_1[0x7b] = 0x290;
    FUN_ffff0000b804cd40(param_1 + 0x7c,uVar1 & 0xfffffff | 0xffff000030000000,0x290);
    FUN_ffff0000b804c33c
              (s__s__d___MMUP__mmup_reg_save_list_ffff0000b809f8db,
               s_mmup_do_regdump_ffff0000b808c833,0x8a,2,DAT_ffff0000b80ea4c0);
    uVar1 = (ulong)DAT_ffff0000b80ea4c0;
    param_1[0x120] = DAT_ffff0000b80ea4c0;
    param_1[0x121] = 0x150;
    FUN_ffff0000b804cd40(param_1 + 0x122,uVar1 & 0xfffffff | 0xffff000030000000,0x150);
  }
  return iVar2;
}


