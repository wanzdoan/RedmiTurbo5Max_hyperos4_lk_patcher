// ffff000090743788  size=824  FUN_ffff000090743788

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090743788(undefined8 param_1)

{
  uint uVar1;
  int iVar2;
  uint *puVar3;
  undefined4 local_34;
  
  local_34 = 0;
  FUN_ffff00009077de54
            (s__s__d___VCP___s__ffff0000907d2ed0,s_vcp_memorydump_size_probe_ffff0000907f2daa,0x48,
             s_vcp_memorydump_size_probe_ffff0000907f2daa);
  iVar2 = FUN_ffff00009076e064(param_1,0xffffffff,s_mediatek_vcp_ffff0000907de2f6);
  if (iVar2 < 0) {
    FUN_ffff00009077de54
              (s__s__d___VCP__get_node_fail__node_ffff0000907d6332,
               s_vcp_memorydump_size_probe_ffff0000907f2daa,0x4c,iVar2);
  }
  FUN_ffff00009077de54
            (s__s__d___VCP__nodeoffset___0x_x_ffff0000907f834c,
             s_vcp_memorydump_size_probe_ffff0000907f2daa,0x4f,iVar2);
  puVar3 = (uint *)FUN_ffff00009076dcd4(param_1,iVar2,s_vcp_support_ffff0000907ec71f,&local_34);
  if (puVar3 == (uint *)0x0) {
    FUN_ffff00009077de54
              (s__VCP___s_Cannot_get_vcp_support_ffff0000907f6555,
               s_vcp_memorydump_size_probe_ffff0000907f2daa);
    _DAT_ffff0000908774b0 = 0;
  }
  else {
    uVar1 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
    _DAT_ffff0000908774b0 = uVar1 >> 0x10 | uVar1 << 0x10;
  }
  FUN_ffff00009077de54(s__VCP__vcp_support____d_ffff0000907d145b);
  puVar3 = (uint *)FUN_ffff00009076dcd4(param_1,iVar2,s_vcp_sram_size_ffff0000907eae3c,&local_34);
  if (puVar3 == (uint *)0x0) {
    FUN_ffff00009077de54
              (s__VCP___s_Cannot_get_sram_size_ffff0000907e4be7,
               s_vcp_memorydump_size_probe_ffff0000907f2daa);
    _DAT_ffff0000908774a8 = 0;
  }
  else {
    uVar1 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
    _DAT_ffff0000908774a8 = uVar1 >> 0x10 | uVar1 << 0x10;
  }
  FUN_ffff00009077de54
            (s__s__d___VCP__sram_size___0x_x_ffff0000907d940f,
             s_vcp_memorydump_size_probe_ffff0000907f2daa,0x5f);
  puVar3 = (uint *)FUN_ffff00009076dcd4(param_1,iVar2,s_memorydump_ffff0000907fba2c,&local_34);
  if (puVar3 == (uint *)0x0) {
    FUN_ffff00009077de54
              (s__VCP___s_Cannot_get_memorydump_s_ffff0000907e7b1d,
               s_vcp_memorydump_size_probe_ffff0000907f2daa);
  }
  else {
    uVar1 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
    _DAT_ffff000090877470 = uVar1 >> 0x10 | uVar1 << 0x10;
    FUN_ffff00009077de54
              (s__s__d___VCP__vcp_dump_prefix__u__ffff0000907f6576,
               s_vcp_memorydump_size_probe_ffff0000907f2daa,0x67,1);
    uVar1 = (puVar3[1] & 0xff00ff00) >> 8 | (puVar3[1] & 0xff00ff) << 8;
    _DAT_ffff000090877478 = uVar1 >> 0x10 | uVar1 << 0x10;
    FUN_ffff00009077de54
              (s__s__d___VCP__vcp_dump_prefix__u__ffff0000907f6576,
               s_vcp_memorydump_size_probe_ffff0000907f2daa,0x67,2);
    _DAT_ffff000090877478 = _DAT_ffff000090877478 + _DAT_ffff000090877470;
    uVar1 = (puVar3[2] & 0xff00ff00) >> 8 | (puVar3[2] & 0xff00ff) << 8;
    _DAT_ffff000090877480 = uVar1 >> 0x10 | uVar1 << 0x10;
    FUN_ffff00009077de54
              (s__s__d___VCP__vcp_dump_prefix__u__ffff0000907f6576,
               s_vcp_memorydump_size_probe_ffff0000907f2daa,0x67,3);
    _DAT_ffff000090877480 = _DAT_ffff000090877480 + _DAT_ffff000090877478;
    uVar1 = (puVar3[3] & 0xff00ff00) >> 8 | (puVar3[3] & 0xff00ff) << 8;
    _DAT_ffff000090877488 = uVar1 >> 0x10 | uVar1 << 0x10;
    FUN_ffff00009077de54
              (s__s__d___VCP__vcp_dump_prefix__u__ffff0000907f6576,
               s_vcp_memorydump_size_probe_ffff0000907f2daa,0x67,4);
    _DAT_ffff000090877488 = _DAT_ffff000090877488 + _DAT_ffff000090877480;
    uVar1 = (puVar3[4] & 0xff00ff00) >> 8 | (puVar3[4] & 0xff00ff) << 8;
    _DAT_ffff000090877490 = uVar1 >> 0x10 | uVar1 << 0x10;
    FUN_ffff00009077de54
              (s__s__d___VCP__vcp_dump_prefix__u__ffff0000907f6576,
               s_vcp_memorydump_size_probe_ffff0000907f2daa,0x67,5);
    _DAT_ffff000090877490 = _DAT_ffff000090877490 + _DAT_ffff000090877488;
  }
  puVar3 = (uint *)FUN_ffff00009076dcd4
                             (param_1,iVar2,s_vcp_secure_dump_offset_ffff0000907daeba,&local_34);
  if (puVar3 == (uint *)0x0) {
    _DAT_ffff000090877498 = 0;
  }
  else {
    uVar1 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
    _DAT_ffff000090877498 = uVar1 >> 0x10 | uVar1 << 0x10;
  }
  FUN_ffff00009077de54
            (s__s__d___VCP__sec_dump_offset___0_ffff0000907f4a79,
             s_vcp_memorydump_size_probe_ffff0000907f2daa,0x71);
  puVar3 = (uint *)FUN_ffff00009076dcd4
                             (param_1,iVar2,s_vcp_secure_dump_size_ffff0000907daed1,&local_34);
  if (puVar3 == (uint *)0x0) {
    _DAT_ffff0000908774a0 = 0;
  }
  else {
    uVar1 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
    _DAT_ffff0000908774a0 = uVar1 >> 0x10 | uVar1 << 0x10;
  }
  FUN_ffff00009077de54
            (s__s__d___VCP__sec_dump_size___0x__ffff0000907dfb69,
             s_vcp_memorydump_size_probe_ffff0000907f2daa,0x78);
  return;
}


