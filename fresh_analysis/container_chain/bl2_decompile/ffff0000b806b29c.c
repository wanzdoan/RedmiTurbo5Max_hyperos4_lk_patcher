
int FUN_ffff0000b806b29c(long param_1,int param_2)

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
  
  uVar1 = DAT_ffff0000b81076ec;
  local_90 = 0;
  uStack_88 = 0;
  local_a0 = 0;
  uStack_98 = 0;
  local_b0 = 0;
  uStack_a8 = 0;
  local_c0 = 0;
  uStack_b8 = 0;
  if (DAT_ffff0000b81b884c == param_2) {
    if ((DAT_ffff0000b81076ec & 0xffffffef) == 0x20) {
      bVar2 = DAT_ffff0000b81076ec == 0x30;
      FUN_ffff0000b804d070(&local_c0,0,0x40);
      iVar3 = FUN_ffff0000b80615f4(param_1,param_2,&local_c0,bVar2);
      if (iVar3 == 0) {
        iVar3 = FUN_ffff0000b8069a90(&DAT_ffff0000b81b8d5c,&local_c0,uVar1);
        uVar1 = DAT_ffff0000b81076ec;
        if (iVar3 == 0) {
LAB_ffff0000b806b43c:
          FUN_ffff0000b804c33c
                    (s___s__image__s_header_auth_pass_ffff0000b809a49c,&DAT_ffff0000b80987e6,
                     &DAT_ffff0000b81b885c);
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
        FUN_ffff0000b804c33c(s___s__SW__Header_auth__ffff0000b80996a9,&DAT_ffff0000b80987e6);
        if ((param_1 != 0) && ((uVar1 & 0xffffffef) == 0x20)) {
          FUN_ffff0000b804d070(&local_80,0,0x40);
          iVar3 = FUN_ffff0000b80616c4(param_1,param_2,&local_80,uVar1 == 0x30);
          if ((iVar3 == 0) &&
             (iVar3 = FUN_ffff0000b8069a90(&DAT_ffff0000b81b8d5c,&local_80,uVar1), iVar3 == 0))
          goto LAB_ffff0000b806b43c;
        }
        iVar3 = 0x101010;
      }
    }
    else {
      iVar3 = -1;
    }
  }
  else {
    FUN_ffff0000b804c33c
              (s___s__hdr_size__0x_x____0x_x__ffff0000b809a47e,&DAT_ffff0000b80987e6,
               DAT_ffff0000b81b884c,param_2);
    iVar3 = 0x101012;
  }
  FUN_ffff0000b804c33c
            (s___s__image__s_header_auth_fail____ffff0000b809e0a7,&DAT_ffff0000b80987e6,
             &DAT_ffff0000b81b885c,iVar3);
  return iVar3;
}


