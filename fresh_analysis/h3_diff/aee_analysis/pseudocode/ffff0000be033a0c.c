
int FUN_ffff0000be033a0c(uint *param_1)

{
  ulong uVar1;
  int iVar2;
  
  iVar2 = DAT_ffff0000be1217e0 - DAT_ffff0000be1217d8;
  FUN_ffff0000be05fd98
            (s__s__d___MMUP__mmup_reg_base_phy_a_ffff0000be0aa538,s_mmup_do_regdump_ffff0000be0ac9e6
             ,0x7d,0x31800000,iVar2);
  if (iVar2 - 0x4001U < 0xffffc000) {
    FUN_ffff0000be05fd98
              (s__MMUP__error___s_reg_size_0x_x___ffff0000be0a813b,
               s_mmup_do_regdump_ffff0000be0ac9e6,iVar2,0x4000);
    iVar2 = 0;
  }
  else {
    FUN_ffff0000be060ae0(param_1,0,iVar2);
    DAT_ffff0000be0d7588 = DAT_ffff0000be0d7588 + 0x31800000;
    DAT_ffff0000be0d758c = DAT_ffff0000be0d758c + 0x31800000;
    DAT_ffff0000be0d7590 = DAT_ffff0000be0d7590 + 0x31800000;
    FUN_ffff0000be05fd98
              (s__s__d___MMUP__mmup_reg_save_list_ffff0000be0c4209,
               s_mmup_do_regdump_ffff0000be0ac9e6,0x8a,0);
    uVar1 = (ulong)DAT_ffff0000be0d7588;
    *param_1 = DAT_ffff0000be0d7588;
    param_1[1] = 0x1e0;
    FUN_ffff0000be0607b0(param_1 + 2,uVar1 & 0xfffffff | 0xffff000030000000,0x1e0);
    FUN_ffff0000be05fd98
              (s__s__d___MMUP__mmup_reg_save_list_ffff0000be0c4209,
               s_mmup_do_regdump_ffff0000be0ac9e6,0x8a,1,DAT_ffff0000be0d758c);
    uVar1 = (ulong)DAT_ffff0000be0d758c;
    param_1[0x7a] = DAT_ffff0000be0d758c;
    param_1[0x7b] = 0x290;
    FUN_ffff0000be0607b0(param_1 + 0x7c,uVar1 & 0xfffffff | 0xffff000030000000,0x290);
    FUN_ffff0000be05fd98
              (s__s__d___MMUP__mmup_reg_save_list_ffff0000be0c4209,
               s_mmup_do_regdump_ffff0000be0ac9e6,0x8a,2,DAT_ffff0000be0d7590);
    uVar1 = (ulong)DAT_ffff0000be0d7590;
    param_1[0x120] = DAT_ffff0000be0d7590;
    param_1[0x121] = 0x150;
    FUN_ffff0000be0607b0(param_1 + 0x122,uVar1 & 0xfffffff | 0xffff000030000000,0x150);
  }
  return iVar2;
}


