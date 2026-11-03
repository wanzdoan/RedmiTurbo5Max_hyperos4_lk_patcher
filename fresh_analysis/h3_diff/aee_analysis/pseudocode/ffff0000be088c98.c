
int FUN_ffff0000be088c98(long param_1,int param_2)

{
  uint uVar1;
  bool bVar2;
  int iVar3;
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
  if (DAT_ffff0000be1b6b0c == param_2) {
    if ((DAT_ffff0000be0f409c & 0xffffffef) == 0x20) {
      bVar2 = DAT_ffff0000be0f409c == 0x30;
      FUN_ffff0000be060ae0(&local_c0,0,0x40);
      iVar3 = FUN_ffff0000be07efb4(param_1,param_2,&local_c0,bVar2);
      if (iVar3 == 0) {
        iVar3 = FUN_ffff0000be08748c(&DAT_ffff0000be1b701c,&local_c0,uVar1);
        uVar1 = DAT_ffff0000be0f409c;
        if (iVar3 == 0) {
LAB_ffff0000be088e38:
          FUN_ffff0000be05fd98
                    (s___s__image__s_header_auth_pass_ffff0000be0bdd25,&DAT_ffff0000be0bb811,
                     &DAT_ffff0000be1b6b1c);
          return 0;
        }
        local_50 = 0;
        uStack_48 = 0;
        local_60 = 0;
        uStack_58 = 0;
        local_70 = 0;
        uStack_68 = 0;
        local_80 = 0;
        uStack_78 = 0;
        FUN_ffff0000be05fd98(s___s__SW__Header_auth__ffff0000be0bca21,&DAT_ffff0000be0bb811);
        if ((param_1 != 0) && ((uVar1 & 0xffffffef) == 0x20)) {
          FUN_ffff0000be060ae0(&local_80,0,0x40);
          iVar3 = FUN_ffff0000be07f084(param_1,param_2,&local_80,uVar1 == 0x30);
          if ((iVar3 == 0) &&
             (iVar3 = FUN_ffff0000be08748c(&DAT_ffff0000be1b701c,&local_80,uVar1), iVar3 == 0))
          goto LAB_ffff0000be088e38;
        }
        iVar3 = 0x101010;
      }
    }
    else {
      iVar3 = -1;
    }
  }
  else {
    FUN_ffff0000be05fd98
              (s___s__hdr_size__0x_x____0x_x__ffff0000be0bdd07,&DAT_ffff0000be0bb811,
               DAT_ffff0000be1b6b0c,param_2);
    iVar3 = 0x101012;
  }
  FUN_ffff0000be05fd98
            (s___s__image__s_header_auth_fail____ffff0000be0c26d4,&DAT_ffff0000be0bb811,
             &DAT_ffff0000be1b6b1c,iVar3);
  return iVar3;
}


