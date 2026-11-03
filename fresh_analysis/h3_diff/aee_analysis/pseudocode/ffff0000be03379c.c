
void FUN_ffff0000be03379c(undefined8 param_1)

{
  int iVar1;
  uint *puVar2;
  uint uVar3;
  undefined4 local_34;
  
  local_34 = 0;
  FUN_ffff0000be05fd98
            (s__s__d___MMUP___s__ffff0000be0ad904,s_mmup_memorydump_size_probe_ffff0000be0bc0c9,0x3f
             ,s_mmup_memorydump_size_probe_ffff0000be0bc0c9);
  iVar1 = FUN_ffff0000be056824(param_1,0xffffffff,s_mediatek_mmup_ffff0000be0c1dff);
  if (iVar1 < 0) {
    FUN_ffff0000be05fd98
              (s__s__d___MMUP__get_node_fail__nod_ffff0000be0c1e0d,
               s_mmup_memorydump_size_probe_ffff0000be0bc0c9,0x43,iVar1);
  }
  FUN_ffff0000be05fd98
            (s__s__d___MMUP__nodeoffset___0x_x_ffff0000be0b9f06,
             s_mmup_memorydump_size_probe_ffff0000be0bc0c9,0x46,iVar1);
  puVar2 = (uint *)FUN_ffff0000be056494(param_1,iVar1,s_memorydump_ffff0000be0c1b90,&local_34);
  if (puVar2 == (uint *)0x0) {
    FUN_ffff0000be05fd98
              (s__MMUP___s_Cannot_get_memorydump_s_ffff0000be0a91c6,
               s_mmup_memorydump_size_probe_ffff0000be0bc0c9);
  }
  else {
    uVar3 = (*puVar2 & 0xff00ff00) >> 8 | (*puVar2 & 0xff00ff) << 8;
    DAT_ffff0000be1217d8 = uVar3 >> 0x10 | uVar3 << 0x10;
    FUN_ffff0000be05fd98
              (s__s__d___MMUP__mmup_dump_prefix___ffff0000be0a4a50,
               s_mmup_memorydump_size_probe_ffff0000be0bc0c9,0x4e,1);
    uVar3 = (puVar2[1] & 0xff00ff00) >> 8 | (puVar2[1] & 0xff00ff) << 8;
    DAT_ffff0000be1217e0 = uVar3 >> 0x10 | uVar3 << 0x10;
    FUN_ffff0000be05fd98
              (s__s__d___MMUP__mmup_dump_prefix___ffff0000be0a4a50,
               s_mmup_memorydump_size_probe_ffff0000be0bc0c9,0x4e,2);
    DAT_ffff0000be1217e0 = DAT_ffff0000be1217e0 + DAT_ffff0000be1217d8;
    uVar3 = (puVar2[2] & 0xff00ff00) >> 8 | (puVar2[2] & 0xff00ff) << 8;
    DAT_ffff0000be1217e8 = uVar3 >> 0x10 | uVar3 << 0x10;
    FUN_ffff0000be05fd98
              (s__s__d___MMUP__mmup_dump_prefix___ffff0000be0a4a50,
               s_mmup_memorydump_size_probe_ffff0000be0bc0c9,0x4e,3);
    DAT_ffff0000be1217e8 = DAT_ffff0000be1217e8 + DAT_ffff0000be1217e0;
    uVar3 = (puVar2[3] & 0xff00ff00) >> 8 | (puVar2[3] & 0xff00ff) << 8;
    DAT_ffff0000be1217f0 = uVar3 >> 0x10 | uVar3 << 0x10;
    FUN_ffff0000be05fd98
              (s__s__d___MMUP__mmup_dump_prefix___ffff0000be0a4a50,
               s_mmup_memorydump_size_probe_ffff0000be0bc0c9,0x4e,4);
    DAT_ffff0000be1217f0 = DAT_ffff0000be1217f0 + DAT_ffff0000be1217e8;
  }
  puVar2 = (uint *)FUN_ffff0000be056494
                             (param_1,iVar1,s_mmup_secure_dump_offset_ffff0000be0be525,&local_34);
  if (puVar2 == (uint *)0x0) {
    FUN_ffff0000be05fd98
              (s__MMUP___s_Cannot_get_secure_dump_ffff0000be0bb073,
               s_mmup_memorydump_size_probe_ffff0000be0bc0c9);
    DAT_ffff0000be1217f8 = 0;
  }
  else {
    uVar3 = (*puVar2 & 0xff00ff00) >> 8 | (*puVar2 & 0xff00ff) << 8;
    DAT_ffff0000be1217f8 = uVar3 >> 0x10 | uVar3 << 0x10;
  }
  FUN_ffff0000be05fd98
            (s__s__d___MMUP__sec_dump_offset___0_ffff0000be0ac9c0,
             s_mmup_memorydump_size_probe_ffff0000be0bc0c9,0x59);
  puVar2 = (uint *)FUN_ffff0000be056494
                             (param_1,iVar1,s_mmup_secure_dump_size_ffff0000be0ab90c,&local_34);
  if (puVar2 == (uint *)0x0) {
    FUN_ffff0000be05fd98
              (s__MMUP___s_Cannot_get_secure_dump_ffff0000be0b9f27,
               s_mmup_memorydump_size_probe_ffff0000be0bc0c9);
    uVar3 = 0;
  }
  else {
    uVar3 = (*puVar2 & 0xff00ff00) >> 8 | (*puVar2 & 0xff00ff) << 8;
    uVar3 = uVar3 >> 0x10 | uVar3 << 0x10;
  }
  FUN_ffff0000be05fd98
            (s__s__d___MMUP__sec_dump_size___0x_ffff0000be0b4229,
             s_mmup_memorydump_size_probe_ffff0000be0bc0c9,0x61,uVar3);
  return;
}


