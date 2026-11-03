// ffff0000907aeaa4  size=452  FUN_ffff0000907aeaa4

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff0000907aeaa4(long param_1,int param_2)

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
  
  uVar1 = DAT_ffff000090837ec4;
  local_90 = 0;
  uStack_88 = 0;
  local_a0 = 0;
  uStack_98 = 0;
  local_b0 = 0;
  uStack_a8 = 0;
  local_c0 = 0;
  uStack_b8 = 0;
  if (_DAT_ffff00009090c614 == param_2) {
    if ((DAT_ffff000090837ec4 & 0xffffffef) == 0x20) {
      bVar2 = DAT_ffff000090837ec4 == 0x30;
      FUN_ffff00009077ee50(&local_c0,0,0x40);
      iVar3 = FUN_ffff0000907a28ec(param_1,param_2,&local_c0,bVar2);
      if (iVar3 == 0) {
        iVar3 = FUN_ffff0000907ad298(0xffff00009090cb24,&local_c0,uVar1);
        uVar1 = DAT_ffff000090837ec4;
        if (iVar3 == 0) {
LAB_ffff0000907aec44:
          FUN_ffff00009077e11c
                    (s___s__image__s_header_auth_pass_ffff0000907f59c4,&DAT_ffff0000907f228a,
                     0xffff00009090c624);
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
        FUN_ffff00009077e11c(s___s__SW__Header_auth__ffff0000907f3bef,&DAT_ffff0000907f228a);
        if ((param_1 != 0) && ((uVar1 & 0xffffffef) == 0x20)) {
          FUN_ffff00009077ee50(&local_80,0,0x40);
          iVar3 = FUN_ffff0000907a29bc(param_1,param_2,&local_80,uVar1 == 0x30);
          if ((iVar3 == 0) &&
             (iVar3 = FUN_ffff0000907ad298(0xffff00009090cb24,&local_80,uVar1), iVar3 == 0))
          goto LAB_ffff0000907aec44;
        }
        iVar3 = 0x101010;
      }
    }
    else {
      iVar3 = -1;
    }
  }
  else {
    FUN_ffff00009077e11c
              (s___s__hdr_size__0x_x____0x_x__ffff0000907f59a6,&DAT_ffff0000907f228a,
               _DAT_ffff00009090c614,param_2);
    iVar3 = 0x101012;
  }
  FUN_ffff00009077e11c
            (s___s__image__s_header_auth_fail____ffff0000907fc94b,&DAT_ffff0000907f228a,
             0xffff00009090c624,iVar3);
  return iVar3;
}


