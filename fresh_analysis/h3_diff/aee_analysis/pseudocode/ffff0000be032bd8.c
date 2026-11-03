
void FUN_ffff0000be032bd8(undefined8 param_1)

{
  uint uVar1;
  int iVar2;
  uint *puVar3;
  undefined4 local_34;
  
  local_34 = 0;
  FUN_ffff0000be05fd98
            (s__s__d___VCP___s__ffff0000be0a5cbc,s_vcp_memorydump_size_probe_ffff0000be0bc065,0x48,
             s_vcp_memorydump_size_probe_ffff0000be0bc065);
  iVar2 = FUN_ffff0000be056824(param_1,0xffffffff,s_mediatek_vcp_ffff0000be0ad8cf);
  if (iVar2 < 0) {
    FUN_ffff0000be05fd98
              (s__s__d___VCP__get_node_fail__node_ffff0000be0a80fb,
               s_vcp_memorydump_size_probe_ffff0000be0bc065,0x4c,iVar2);
  }
  FUN_ffff0000be05fd98
            (s__s__d___VCP__nodeoffset___0x_x_ffff0000be0bf931,
             s_vcp_memorydump_size_probe_ffff0000be0bc065,0x4f,iVar2);
  puVar3 = (uint *)FUN_ffff0000be056494(param_1,iVar2,s_vcp_support_ffff0000be0b78f5,&local_34);
  if (puVar3 == (uint *)0x0) {
    FUN_ffff0000be05fd98
              (s__VCP___s_Cannot_get_vcp_support_ffff0000be0be4db,
               s_vcp_memorydump_size_probe_ffff0000be0bc065);
    DAT_ffff0000be1217c8 = 0;
  }
  else {
    uVar1 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
    DAT_ffff0000be1217c8 = uVar1 >> 0x10 | uVar1 << 0x10;
  }
  FUN_ffff0000be05fd98(s__VCP__vcp_support____d_ffff0000be0a4a10);
  puVar3 = (uint *)FUN_ffff0000be056494(param_1,iVar2,s_vcp_sram_size_ffff0000be0b677d,&local_34);
  if (puVar3 == (uint *)0x0) {
    FUN_ffff0000be05fd98
              (s__VCP___s_Cannot_get_sram_size_ffff0000be0b1feb,
               s_vcp_memorydump_size_probe_ffff0000be0bc065);
    DAT_ffff0000be1217c0 = 0;
  }
  else {
    uVar1 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
    DAT_ffff0000be1217c0 = uVar1 >> 0x10 | uVar1 << 0x10;
  }
  FUN_ffff0000be05fd98
            (s__s__d___VCP__sram_size___0x_x_ffff0000be0aa519,
             s_vcp_memorydump_size_probe_ffff0000be0bc065,0x5f);
  puVar3 = (uint *)FUN_ffff0000be056494(param_1,iVar2,s_memorydump_ffff0000be0c1b90,&local_34);
  if (puVar3 == (uint *)0x0) {
    FUN_ffff0000be05fd98
              (s__VCP___s_Cannot_get_memorydump_s_ffff0000be0b4204,
               s_vcp_memorydump_size_probe_ffff0000be0bc065);
  }
  else {
    uVar1 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
    DAT_ffff0000be121788 = uVar1 >> 0x10 | uVar1 << 0x10;
    FUN_ffff0000be05fd98
              (s__s__d___VCP__vcp_dump_prefix__u__ffff0000be0be4fc,
               s_vcp_memorydump_size_probe_ffff0000be0bc065,0x67,1);
    uVar1 = (puVar3[1] & 0xff00ff00) >> 8 | (puVar3[1] & 0xff00ff) << 8;
    DAT_ffff0000be121790 = uVar1 >> 0x10 | uVar1 << 0x10;
    FUN_ffff0000be05fd98
              (s__s__d___VCP__vcp_dump_prefix__u__ffff0000be0be4fc,
               s_vcp_memorydump_size_probe_ffff0000be0bc065,0x67,2);
    DAT_ffff0000be121790 = DAT_ffff0000be121790 + DAT_ffff0000be121788;
    uVar1 = (puVar3[2] & 0xff00ff00) >> 8 | (puVar3[2] & 0xff00ff) << 8;
    DAT_ffff0000be121798 = uVar1 >> 0x10 | uVar1 << 0x10;
    FUN_ffff0000be05fd98
              (s__s__d___VCP__vcp_dump_prefix__u__ffff0000be0be4fc,
               s_vcp_memorydump_size_probe_ffff0000be0bc065,0x67,3);
    DAT_ffff0000be121798 = DAT_ffff0000be121798 + DAT_ffff0000be121790;
    uVar1 = (puVar3[3] & 0xff00ff00) >> 8 | (puVar3[3] & 0xff00ff) << 8;
    DAT_ffff0000be1217a0 = uVar1 >> 0x10 | uVar1 << 0x10;
    FUN_ffff0000be05fd98
              (s__s__d___VCP__vcp_dump_prefix__u__ffff0000be0be4fc,
               s_vcp_memorydump_size_probe_ffff0000be0bc065,0x67,4);
    DAT_ffff0000be1217a0 = DAT_ffff0000be1217a0 + DAT_ffff0000be121798;
    uVar1 = (puVar3[4] & 0xff00ff00) >> 8 | (puVar3[4] & 0xff00ff) << 8;
    DAT_ffff0000be1217a8 = uVar1 >> 0x10 | uVar1 << 0x10;
    FUN_ffff0000be05fd98
              (s__s__d___VCP__vcp_dump_prefix__u__ffff0000be0be4fc,
               s_vcp_memorydump_size_probe_ffff0000be0bc065,0x67,5);
    DAT_ffff0000be1217a8 = DAT_ffff0000be1217a8 + DAT_ffff0000be1217a0;
  }
  puVar3 = (uint *)FUN_ffff0000be056494
                             (param_1,iVar2,s_vcp_secure_dump_offset_ffff0000be0ab8b3,&local_34);
  if (puVar3 == (uint *)0x0) {
    DAT_ffff0000be1217b0 = 0;
  }
  else {
    uVar1 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
    DAT_ffff0000be1217b0 = uVar1 >> 0x10 | uVar1 << 0x10;
  }
  FUN_ffff0000be05fd98
            (s__s__d___VCP__sec_dump_offset___0_ffff0000be0bd360,
             s_vcp_memorydump_size_probe_ffff0000be0bc065,0x71);
  puVar3 = (uint *)FUN_ffff0000be056494
                             (param_1,iVar2,s_vcp_secure_dump_size_ffff0000be0ab8ca,&local_34);
  if (puVar3 == (uint *)0x0) {
    DAT_ffff0000be1217b8 = 0;
  }
  else {
    uVar1 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
    DAT_ffff0000be1217b8 = uVar1 >> 0x10 | uVar1 << 0x10;
  }
  FUN_ffff0000be05fd98
            (s__s__d___VCP__sec_dump_size___0x__ffff0000be0ae972,
             s_vcp_memorydump_size_probe_ffff0000be0bc065,0x78);
  return;
}


