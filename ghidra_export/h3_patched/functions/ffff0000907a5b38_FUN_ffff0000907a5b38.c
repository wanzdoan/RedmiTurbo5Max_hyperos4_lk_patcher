// ffff0000907a5b38  size=968  FUN_ffff0000907a5b38

int FUN_ffff0000907a5b38(long *param_1,char param_2)

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
    iVar2 = FUN_ffff00009077e820
                      ((long)param_1 + 0x36,&local_120,*(undefined1 *)((long)param_1 + 0x35));
    if (iVar2 != 0) {
      FUN_ffff00009077de54(s__SEC__FE_remote_decryption__ffff0000907e05bf);
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
      FUN_ffff00009077e858(&local_c0,(long)param_1 + 0x15,*(undefined1 *)((long)param_1 + 0x14));
      FUN_ffff00009077e858(&local_100,(long)param_1 + 0x36,*(undefined1 *)((long)param_1 + 0x35));
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
LAB_ffff0000907a5db8:
        FUN_ffff00009077eb88(&local_80,0,0x40);
LAB_ffff0000907a5dcc:
        FUN_ffff0000907aacd0
                  (&local_c0,*(undefined1 *)((long)param_1 + 0x14),&local_100,lVar1,&local_c0);
        iVar2 = FUN_ffff0000907aacd0(*param_1,(int)param_1[2],&local_c0,lVar1,param_1[1]);
      }
      else {
        if (param_2 != '\x01') {
          FUN_ffff00009077de54(s_WARNING__Not_Support_Key_Wrapper_ffff0000907e218b);
          goto LAB_ffff0000907a5db8;
        }
        iVar2 = FUN_ffff0000907b6074(3,&local_80,0x20);
        if (iVar2 == 0) {
          iVar2 = FUN_ffff0000907aacd0(&local_100,0x20,&local_80,lVar1,&local_100);
          FUN_ffff00009077eb88(&local_80,0,0x40);
          if (iVar2 != 0) goto LAB_ffff0000907a5dfc;
          goto LAB_ffff0000907a5dcc;
        }
        FUN_ffff00009077de54(s_Unwrap_key_generation_fail___d_ffff0000907d061f,iVar2);
        FUN_ffff00009077eb88(&local_80,0,0x40);
      }
LAB_ffff0000907a5dfc:
      FUN_ffff00009077eb88(&local_c0,0,0x40);
      puVar3 = &local_100;
      goto LAB_ffff0000907a5ed4;
    }
    FUN_ffff00009077de54(s__SEC__FE_legacy_decryption__ffff0000907d6fb6);
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
    FUN_ffff00009077e858(&local_c0,(long)param_1 + 0x15,*(undefined1 *)((long)param_1 + 0x14));
    local_50 = 0;
    uStack_48 = 0;
    local_60 = 0;
    uStack_58 = 0;
    local_70 = 0;
    uStack_68 = 0;
    local_80 = 0;
    uStack_78 = 0;
    if (param_2 == '\0') {
LAB_ffff0000907a5e20:
      FUN_ffff00009077eb88(&local_80,0,0x40);
    }
    else {
      if (param_2 != '\x01') {
        FUN_ffff00009077de54(s_WARNING__Not_Support_Key_Wrapper_ffff0000907e218b);
        goto LAB_ffff0000907a5e20;
      }
      iVar2 = FUN_ffff0000907b6074(3,&local_80,0x20);
      if (iVar2 != 0) {
        FUN_ffff00009077de54(s_Unwrap_key_generation_fail___d_ffff0000907d061f,iVar2);
        FUN_ffff00009077eb88(&local_80,0,0x40);
LAB_ffff0000907a5ebc:
        FUN_ffff00009077de54(s__s_Key_unwrap_fail__ffff0000907d53c5,s__crypto_hw__ffff0000907d8867);
        goto LAB_ffff0000907a5ed0;
      }
      iVar2 = FUN_ffff0000907aacd0(&local_c0,0x20,&local_80,(long)param_1 + 0x56,&local_c0);
      FUN_ffff00009077eb88(&local_80,0,0x40);
      if (iVar2 != 0) goto LAB_ffff0000907a5ebc;
    }
    iVar2 = FUN_ffff0000907aacd0(*param_1,(int)param_1[2],&local_c0,(long)param_1 + 0x56,param_1[1])
    ;
  }
  else {
    FUN_ffff00009077de54(s__SEC__FE_30_decryption__ffff0000907ea219);
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
    FUN_ffff00009077e858(&local_80,(long)param_1 + 0x15,*(undefined1 *)((long)param_1 + 0x14));
    FUN_ffff00009077e858(&local_c0,(long)param_1 + 0x36,*(undefined1 *)((long)param_1 + 0x35));
    iVar2 = FUN_ffff0000907b6074(3,&local_c0,0x20);
    if (iVar2 == 0) {
      FUN_ffff0000907aacd0
                (&local_80,*(undefined1 *)((long)param_1 + 0x14),&local_c0,(long)param_1 + 0x56,
                 &local_80);
      iVar2 = FUN_ffff0000907aacd0
                        (*param_1,(int)param_1[2],&local_80,(long)param_1 + 0x66,param_1[1]);
    }
    FUN_ffff00009077eb88(&local_80,0,0x40);
  }
LAB_ffff0000907a5ed0:
  puVar3 = &local_c0;
LAB_ffff0000907a5ed4:
  FUN_ffff00009077eb88(puVar3,0,0x40);
  return iVar2;
}


