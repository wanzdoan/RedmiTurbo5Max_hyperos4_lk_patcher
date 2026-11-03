
int FUN_ffff0000be06742c
              (long param_1,undefined8 param_2,long param_3,int param_4,long param_5,int param_6,
              long param_7,int *param_8)

{
  int iVar1;
  undefined8 local_160;
  undefined8 uStack_158;
  undefined8 local_150;
  undefined8 uStack_148;
  undefined8 local_140;
  undefined8 uStack_138;
  undefined8 local_130;
  undefined8 uStack_128;
  undefined8 local_120;
  undefined8 uStack_118;
  undefined8 local_110;
  undefined8 uStack_108;
  undefined8 local_100;
  undefined8 uStack_f8;
  undefined8 local_f0;
  undefined8 uStack_e8;
  undefined8 local_e0;
  undefined8 uStack_d8;
  undefined8 local_d0;
  undefined8 uStack_c8;
  undefined8 local_c0;
  undefined8 uStack_b8;
  undefined8 local_b0;
  undefined8 uStack_a8;
  undefined8 local_a0;
  undefined8 uStack_98;
  undefined8 local_90;
  undefined8 uStack_88;
  undefined8 local_80;
  undefined8 uStack_78;
  undefined8 local_70;
  undefined8 uStack_68;
  
  local_70 = 0;
  uStack_68 = 0;
  local_80 = 0;
  uStack_78 = 0;
  local_90 = 0;
  uStack_88 = 0;
  local_a0 = 0;
  uStack_98 = 0;
  local_b0 = 0;
  uStack_a8 = 0;
  local_c0 = 0;
  uStack_b8 = 0;
  local_d0 = 0;
  uStack_c8 = 0;
  local_e0 = 0;
  uStack_d8 = 0;
  local_f0 = 0;
  uStack_e8 = 0;
  local_100 = 0;
  uStack_f8 = 0;
  local_110 = 0;
  uStack_108 = 0;
  local_120 = 0;
  uStack_118 = 0;
  local_130 = 0;
  uStack_128 = 0;
  local_140 = 0;
  uStack_138 = 0;
  local_150 = 0;
  uStack_148 = 0;
  local_160 = 0;
  uStack_158 = 0;
  if (param_1 == 0) {
    iVar1 = 0x10b00f;
  }
  else if ((int)param_2 == 0) {
    iVar1 = 0x10b010;
  }
  else if (param_3 == 0) {
    iVar1 = 0x10b013;
  }
  else if (param_4 == 0) {
    iVar1 = 0x10b014;
  }
  else if (param_5 == 0) {
    iVar1 = 0x10b015;
  }
  else if (param_6 == 0) {
    iVar1 = 0x10b016;
  }
  else if (param_7 == 0) {
    iVar1 = 0x10b011;
  }
  else if (param_8 == (int *)0x0) {
    iVar1 = 0x10b012;
  }
  else {
    iVar1 = 0x10b012;
    if ((*param_8 != 0) &&
       (iVar1 = FUN_ffff0000be066f64(param_1,param_2,&local_160,0x100), iVar1 == 0)) {
      iVar1 = thunk_FUN_ffff0000be07cfbc
                        (&local_160,0x100,param_7,param_8,param_3,param_4,param_5,param_6);
      if (iVar1 == 0) {
        return 0;
      }
      FUN_ffff0000be05fd98(s__rsa_oeap__rsa_exptmod_fail__0x__ffff0000be0b7faf,iVar1);
      iVar1 = 0x10b019;
    }
  }
  FUN_ffff0000be05fd98(s__rsa_oeap__encrypt_fail__0x_x__ffff0000be0bd9e4,iVar1);
  return iVar1;
}


