// ffff000090743a20  size=824  FUN_ffff000090743a20

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090743a20(undefined8 param_1)

{
  uint uVar1;
  int iVar2;
  uint *puVar3;
  undefined4 local_34;
  
  local_34 = 0;
  FUN_ffff00009077e11c
            (s__s__d___VCP___s__ffff0000907d2f0d,s_vcp_memorydump_size_probe_ffff0000907f2f4d,0x48,
             s_vcp_memorydump_size_probe_ffff0000907f2f4d);
  iVar2 = FUN_ffff00009076e32c(param_1,0xffffffff,s_mediatek_vcp_ffff0000907de3b0);
  if (iVar2 < 0) {
    FUN_ffff00009077e11c
              (s__s__d___VCP__get_node_fail__node_ffff0000907d63c8,
               s_vcp_memorydump_size_probe_ffff0000907f2f4d,0x4c,iVar2);
  }
  FUN_ffff00009077e11c
            (s__s__d___VCP__nodeoffset___0x_x_ffff0000907f84e8,
             s_vcp_memorydump_size_probe_ffff0000907f2f4d,0x4f,iVar2);
  puVar3 = (uint *)FUN_ffff00009076df9c(param_1,iVar2,s_vcp_support_ffff0000907ec8b9,&local_34);
  if (puVar3 == (uint *)0x0) {
    FUN_ffff00009077e11c
              (s__VCP___s_Cannot_get_vcp_support_ffff0000907f66f6,
               s_vcp_memorydump_size_probe_ffff0000907f2f4d);
    _DAT_ffff000090877548 = 0;
  }
  else {
    uVar1 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
    _DAT_ffff000090877548 = uVar1 >> 0x10 | uVar1 << 0x10;
  }
  FUN_ffff00009077e11c(s__VCP__vcp_support____d_ffff0000907d1498);
  puVar3 = (uint *)FUN_ffff00009076df9c(param_1,iVar2,s_vcp_sram_size_ffff0000907eafd6,&local_34);
  if (puVar3 == (uint *)0x0) {
    FUN_ffff00009077e11c
              (s__VCP___s_Cannot_get_sram_size_ffff0000907e4d02,
               s_vcp_memorydump_size_probe_ffff0000907f2f4d);
    _DAT_ffff000090877540 = 0;
  }
  else {
    uVar1 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
    _DAT_ffff000090877540 = uVar1 >> 0x10 | uVar1 << 0x10;
  }
  FUN_ffff00009077e11c
            (s__s__d___VCP__sram_size___0x_x_ffff0000907d94a6,
             s_vcp_memorydump_size_probe_ffff0000907f2f4d,0x5f);
  puVar3 = (uint *)FUN_ffff00009076df9c(param_1,iVar2,s_memorydump_ffff0000907fbbc8,&local_34);
  if (puVar3 == (uint *)0x0) {
    FUN_ffff00009077e11c
              (s__VCP___s_Cannot_get_memorydump_s_ffff0000907e7c78,
               s_vcp_memorydump_size_probe_ffff0000907f2f4d);
  }
  else {
    uVar1 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
    _DAT_ffff000090877508 = uVar1 >> 0x10 | uVar1 << 0x10;
    FUN_ffff00009077e11c
              (s__s__d___VCP__vcp_dump_prefix__u__ffff0000907f6717,
               s_vcp_memorydump_size_probe_ffff0000907f2f4d,0x67,1);
    uVar1 = (puVar3[1] & 0xff00ff00) >> 8 | (puVar3[1] & 0xff00ff) << 8;
    _DAT_ffff000090877510 = uVar1 >> 0x10 | uVar1 << 0x10;
    FUN_ffff00009077e11c
              (s__s__d___VCP__vcp_dump_prefix__u__ffff0000907f6717,
               s_vcp_memorydump_size_probe_ffff0000907f2f4d,0x67,2);
    _DAT_ffff000090877510 = _DAT_ffff000090877510 + _DAT_ffff000090877508;
    uVar1 = (puVar3[2] & 0xff00ff00) >> 8 | (puVar3[2] & 0xff00ff) << 8;
    _DAT_ffff000090877518 = uVar1 >> 0x10 | uVar1 << 0x10;
    FUN_ffff00009077e11c
              (s__s__d___VCP__vcp_dump_prefix__u__ffff0000907f6717,
               s_vcp_memorydump_size_probe_ffff0000907f2f4d,0x67,3);
    _DAT_ffff000090877518 = _DAT_ffff000090877518 + _DAT_ffff000090877510;
    uVar1 = (puVar3[3] & 0xff00ff00) >> 8 | (puVar3[3] & 0xff00ff) << 8;
    _DAT_ffff000090877520 = uVar1 >> 0x10 | uVar1 << 0x10;
    FUN_ffff00009077e11c
              (s__s__d___VCP__vcp_dump_prefix__u__ffff0000907f6717,
               s_vcp_memorydump_size_probe_ffff0000907f2f4d,0x67,4);
    _DAT_ffff000090877520 = _DAT_ffff000090877520 + _DAT_ffff000090877518;
    uVar1 = (puVar3[4] & 0xff00ff00) >> 8 | (puVar3[4] & 0xff00ff) << 8;
    _DAT_ffff000090877528 = uVar1 >> 0x10 | uVar1 << 0x10;
    FUN_ffff00009077e11c
              (s__s__d___VCP__vcp_dump_prefix__u__ffff0000907f6717,
               s_vcp_memorydump_size_probe_ffff0000907f2f4d,0x67,5);
    _DAT_ffff000090877528 = _DAT_ffff000090877528 + _DAT_ffff000090877520;
  }
  puVar3 = (uint *)FUN_ffff00009076df9c
                             (param_1,iVar2,s_vcp_secure_dump_offset_ffff0000907dafdc,&local_34);
  if (puVar3 == (uint *)0x0) {
    _DAT_ffff000090877530 = 0;
  }
  else {
    uVar1 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
    _DAT_ffff000090877530 = uVar1 >> 0x10 | uVar1 << 0x10;
  }
  FUN_ffff00009077e11c
            (s__s__d___VCP__sec_dump_offset___0_ffff0000907f4bfd,
             s_vcp_memorydump_size_probe_ffff0000907f2f4d,0x71);
  puVar3 = (uint *)FUN_ffff00009076df9c
                             (param_1,iVar2,s_vcp_secure_dump_size_ffff0000907daff3,&local_34);
  if (puVar3 == (uint *)0x0) {
    _DAT_ffff000090877538 = 0;
  }
  else {
    uVar1 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
    _DAT_ffff000090877538 = uVar1 >> 0x10 | uVar1 << 0x10;
  }
  FUN_ffff00009077e11c
            (s__s__d___VCP__sec_dump_size___0x__ffff0000907dfc23,
             s_vcp_memorydump_size_probe_ffff0000907f2f4d,0x78);
  return;
}


