
int FUN_ffff0000be080dec(long *param_1,char param_2)

{
  long lVar1;
  int iVar2;
  undefined8 *puVar3;
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
  undefined8 local_60;
  undefined8 uStack_58;
  undefined8 local_50;
  undefined8 uStack_48;
  
  local_110 = 0;
  uStack_108 = 0;
  local_120 = 0;
  uStack_118 = 0;
  if (*(byte *)((long)param_1 + 0x76) < 2) {
    iVar2 = FUN_ffff0000be060778
                      ((long)param_1 + 0x36,&local_120,*(undefined1 *)((long)param_1 + 0x35));
    if (iVar2 != 0) {
      FUN_ffff0000be05fd98(s__SEC__FE_remote_decryption__ffff0000be0af1b5);
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
      if (*param_1 == 0) {
        return 0x10c004;
      }
      if (param_1[1] == 0) {
        return 0x10c004;
      }
      FUN_ffff0000be0607b0(&local_c0,(long)param_1 + 0x15,*(undefined1 *)((long)param_1 + 0x14));
      FUN_ffff0000be0607b0(&local_100,(long)param_1 + 0x36,*(undefined1 *)((long)param_1 + 0x35));
      lVar1 = (long)param_1 + 0x56;
      local_50 = 0;
      uStack_48 = 0;
      local_60 = 0;
      uStack_58 = 0;
      local_70 = 0;
      uStack_68 = 0;
      local_80 = 0;
      uStack_78 = 0;
      if (param_2 == '\0') {
LAB_ffff0000be08106c:
        FUN_ffff0000be060ae0(&local_80,0,0x40);
LAB_ffff0000be081080:
        FUN_ffff0000be085f84
                  (&local_c0,*(undefined1 *)((long)param_1 + 0x14),&local_100,lVar1,&local_c0);
        iVar2 = FUN_ffff0000be085f84(*param_1,(int)param_1[2],&local_c0,lVar1,param_1[1]);
      }
      else {
        if (param_2 != '\x01') {
          FUN_ffff0000be05fd98(s_WARNING__Not_Support_Key_Wrapper_ffff0000be0b0679);
          goto LAB_ffff0000be08106c;
        }
        iVar2 = FUN_ffff0000be090708(3,&local_80,0x20);
        if (iVar2 == 0) {
          iVar2 = FUN_ffff0000be085f84(&local_100,0x20,&local_80,lVar1,&local_100);
          FUN_ffff0000be060ae0(&local_80,0,0x40);
          if (iVar2 != 0) goto LAB_ffff0000be0810b0;
          goto LAB_ffff0000be081080;
        }
        FUN_ffff0000be05fd98(s_Unwrap_key_generation_fail___d_ffff0000be0a3eff,iVar2);
        FUN_ffff0000be060ae0(&local_80,0,0x40);
      }
LAB_ffff0000be0810b0:
      FUN_ffff0000be060ae0(&local_c0,0,0x40);
      puVar3 = &local_100;
      goto LAB_ffff0000be081188;
    }
    FUN_ffff0000be05fd98(s__SEC__FE_legacy_decryption__ffff0000be0a8a5c);
    local_90 = 0;
    uStack_88 = 0;
    local_a0 = 0;
    uStack_98 = 0;
    local_b0 = 0;
    uStack_a8 = 0;
    local_c0 = 0;
    uStack_b8 = 0;
    if (*param_1 == 0) {
      return 0x10c004;
    }
    if (param_1[1] == 0) {
      return 0x10c004;
    }
    FUN_ffff0000be0607b0(&local_c0,(long)param_1 + 0x15,*(undefined1 *)((long)param_1 + 0x14));
    local_50 = 0;
    uStack_48 = 0;
    local_60 = 0;
    uStack_58 = 0;
    local_70 = 0;
    uStack_68 = 0;
    local_80 = 0;
    uStack_78 = 0;
    if (param_2 == '\0') {
LAB_ffff0000be0810d4:
      FUN_ffff0000be060ae0(&local_80,0,0x40);
    }
    else {
      if (param_2 != '\x01') {
        FUN_ffff0000be05fd98(s_WARNING__Not_Support_Key_Wrapper_ffff0000be0b0679);
        goto LAB_ffff0000be0810d4;
      }
      iVar2 = FUN_ffff0000be090708(3,&local_80,0x20);
      if (iVar2 != 0) {
        FUN_ffff0000be05fd98(s_Unwrap_key_generation_fail___d_ffff0000be0a3eff,iVar2);
        FUN_ffff0000be060ae0(&local_80,0,0x40);
LAB_ffff0000be081170:
        FUN_ffff0000be05fd98(s__s_Key_unwrap_fail__ffff0000be0a7833,s__crypto_hw__ffff0000be0a9c2b);
        goto LAB_ffff0000be081184;
      }
      iVar2 = FUN_ffff0000be085f84(&local_c0,0x20,&local_80,(long)param_1 + 0x56,&local_c0);
      FUN_ffff0000be060ae0(&local_80,0,0x40);
      if (iVar2 != 0) goto LAB_ffff0000be081170;
    }
    iVar2 = FUN_ffff0000be085f84(*param_1,(int)param_1[2],&local_c0,(long)param_1 + 0x56,param_1[1])
    ;
  }
  else {
    FUN_ffff0000be05fd98(s__SEC__FE_30_decryption__ffff0000be0b5e19);
    local_50 = 0;
    uStack_48 = 0;
    local_60 = 0;
    uStack_58 = 0;
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
    if (*param_1 == 0) {
      return 0x10c004;
    }
    if (param_1[1] == 0) {
      return 0x10c004;
    }
    FUN_ffff0000be0607b0(&local_80,(long)param_1 + 0x15,*(undefined1 *)((long)param_1 + 0x14));
    FUN_ffff0000be0607b0(&local_c0,(long)param_1 + 0x36,*(undefined1 *)((long)param_1 + 0x35));
    iVar2 = FUN_ffff0000be090708(3,&local_c0,0x20);
    if (iVar2 == 0) {
      FUN_ffff0000be085f84
                (&local_80,*(undefined1 *)((long)param_1 + 0x14),&local_c0,(long)param_1 + 0x56,
                 &local_80);
      iVar2 = FUN_ffff0000be085f84
                        (*param_1,(int)param_1[2],&local_80,(long)param_1 + 0x66,param_1[1]);
    }
    FUN_ffff0000be060ae0(&local_80,0,0x40);
  }
LAB_ffff0000be081184:
  puVar3 = &local_c0;
LAB_ffff0000be081188:
  FUN_ffff0000be060ae0(puVar3,0,0x40);
  return iVar2;
}


