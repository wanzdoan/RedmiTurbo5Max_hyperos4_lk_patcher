
int FUN_ffff0000be088e5c(long param_1,ulong param_2)

{
  undefined4 uVar1;
  int iVar2;
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
  undefined8 local_60;
  undefined8 uStack_58;
  undefined8 local_50;
  undefined8 uStack_48;
  
  uVar1 = DAT_ffff0000be0f409c;
  local_90 = 0;
  uStack_88 = 0;
  local_a0 = 0;
  uStack_98 = 0;
  local_b0 = 0;
  uStack_a8 = 0;
  local_c0 = 0;
  uStack_b8 = 0;
  iVar2 = FUN_ffff0000be0881bc
                    (param_1,param_2,DAT_ffff0000be1b6b14 - (int)param_2,&local_c0,
                     DAT_ffff0000be0f409c);
  if (iVar2 == 0) {
    iVar2 = FUN_ffff0000be08748c(&DAT_ffff0000be1b6fe8,&local_c0,uVar1);
    uVar1 = DAT_ffff0000be0f409c;
    if (iVar2 != 0) {
      local_50 = 0;
      uStack_48 = 0;
      local_60 = 0;
      uStack_58 = 0;
      local_70 = 0;
      uStack_68 = 0;
      local_80 = 0;
      uStack_78 = 0;
      FUN_ffff0000be05fd98(s___s__SW__Image_auth__ffff0000be0ac133,&DAT_ffff0000be0bb811);
      if (((param_1 == 0) ||
          (iVar2 = FUN_ffff0000be08831c
                             (param_1,param_2 & 0xffffffff,DAT_ffff0000be1b6b14 - (int)param_2,
                              &local_80,uVar1), iVar2 != 0)) ||
         (iVar2 = FUN_ffff0000be08748c(&DAT_ffff0000be1b6fe8,&local_80,uVar1), iVar2 != 0)) {
        iVar2 = 0x101011;
        goto LAB_ffff0000be088f3c;
      }
    }
    FUN_ffff0000be05fd98
              (s___s__image__s_auth_pass_ffff0000be0a3f50,&DAT_ffff0000be0bb811,
               &DAT_ffff0000be1b6b1c);
    iVar2 = FUN_ffff0000be087e8c(param_1,param_2 & 0xffffffff);
    if (DAT_ffff0000be1b6ac4 != 0) {
      FUN_ffff0000be0357ec();
    }
  }
  else {
LAB_ffff0000be088f3c:
    FUN_ffff0000be05fd98
              (s___s__image__s_auth_fail__0x_x__ffff0000be0bc9ca,&DAT_ffff0000be0bb811,
               &DAT_ffff0000be1b6b1c,iVar2);
    FUN_ffff0000be0894ac();
  }
  FUN_ffff0000be091db4();
  return iVar2;
}


