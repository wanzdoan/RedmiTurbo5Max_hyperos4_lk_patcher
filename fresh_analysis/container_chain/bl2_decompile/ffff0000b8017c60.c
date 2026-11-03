
undefined8 FUN_ffff0000b8017c60(void)

{
  char *pcVar1;
  uint uVar2;
  undefined8 uVar3;
  
  uVar2 = *(uint *)(DAT_ffff0000b80e8d58 + 0x694);
  if (DAT_ffff0000b811b0f4 < 2) {
    uVar3 = 0xffffffff;
  }
  else {
    pcVar1 = s_disabled_ffff0000b8097e51;
    if ((*(uint *)(DAT_ffff0000b80e8d58 + 0x690) & 0x20) != 0) {
      pcVar1 = s_enabled_ffff0000b809b6a2;
    }
    FUN_ffff0000b804c33c(s__GZ_INIT__REMAP__MT_V3__Domain_R_ffff0000b809438b,pcVar1);
    pcVar1 = s_disabled_ffff0000b8097e51;
    if ((uVar2 & 0x20) != 0) {
      pcVar1 = s_enabled_ffff0000b809b6a2;
    }
    FUN_ffff0000b804c33c(s__GZ_INIT__REMAP__MT_V3__Domain_R_ffff0000b808eaf9,pcVar1);
    FUN_ffff0000b804c33c(s__GZ_INIT__REMAP__MT_V3__DDR_rema_ffff0000b808b6c8);
    FUN_ffff0000b804c33c(s__GZ_INIT__REMAP__MT_V3__VMID_u__0_ffff0000b808b6f3,0,0);
    FUN_ffff0000b804c33c(s__GZ_INIT__REMAP__MT_V3__VMID_u__0_ffff0000b808b6f3,1,0x1000000000);
    FUN_ffff0000b804c33c(s__GZ_INIT__REMAP__MT_V3__IO_remap_ffff0000b8097e5a);
    FUN_ffff0000b804c33c(s__GZ_INIT__REMAP__MT_V3__VMID_u__0_ffff0000b808b6f3,0,0);
    FUN_ffff0000b804c33c(s__GZ_INIT__REMAP__MT_V3__VMID_u__0_ffff0000b808b6f3,1,0x1000000000);
    FUN_ffff0000b804c33c(s__GZ_INIT__REMAP__MT_V3__IO_VMID__ffff0000b808c57d);
    FUN_ffff0000b804c33c(s__GZ_INIT__REMAP__MT_V3__VMID_u__0_ffff0000b808b6f3,0,0x20000000);
    FUN_ffff0000b804c33c(s__GZ_INIT__REMAP__MT_V3__VMID_u__0_ffff0000b808b6f3,1,0x1020000000);
    FUN_ffff0000b804c33c(s__GZ_INIT__REMAP__MT_V3__Domain_r_ffff0000b8097e84,&DAT_ffff0000b809b6aa);
    FUN_ffff0000b804c33c(s__GZ_INIT__REMAP__MT_V3__VMID__u__ffff0000b808c5b0,0,8);
    FUN_ffff0000b804c33c(s__GZ_INIT__REMAP__MT_V3__VMID__u__ffff0000b808c5b0,1,9);
    FUN_ffff0000b804c33c(s__GZ_INIT__REMAP__MT_V3__VMID__u__ffff0000b808c5b0,2,0);
    FUN_ffff0000b804c33c(s__GZ_INIT__REMAP__MT_V3__VMID__u__ffff0000b808c5b0,3,0);
    uVar3 = 0;
  }
  return uVar3;
}


