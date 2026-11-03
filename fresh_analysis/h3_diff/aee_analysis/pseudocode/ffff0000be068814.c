
void FUN_ffff0000be068814(long param_1)

{
  uint uVar1;
  bool bVar2;
  undefined4 uVar3;
  int iVar4;
  undefined4 uVar5;
  uint *puVar6;
  long lVar7;
  int local_24;
  
  local_24 = 0;
  if (param_1 != 0) {
    uVar3 = FUN_ffff0000be056824(param_1,0xffffffff,s_mediatek_scp_ffff0000be0b40da);
    puVar6 = (uint *)FUN_ffff0000be056494(param_1,uVar3,s_scp_sram_size_ffff0000be0beda7,&local_24);
    if (puVar6 == (uint *)0x0) {
      DAT_ffff0000be132608 = 0;
    }
    else {
      uVar1 = (*puVar6 & 0xff00ff00) >> 8 | (*puVar6 & 0xff00ff) << 8;
      DAT_ffff0000be132608 = uVar1 >> 0x10 | uVar1 << 0x10;
    }
    puVar6 = (uint *)FUN_ffff0000be056494(param_1,uVar3,s_scp_dram_size_ffff0000be0c24ed,&local_24);
    if (puVar6 == (uint *)0x0) {
      DAT_ffff0000be132620 = 0;
    }
    else {
      uVar1 = (*puVar6 & 0xff00ff00) >> 8 | (*puVar6 & 0xff00ff) << 8;
      DAT_ffff0000be132620 = uVar1 >> 0x10 | uVar1 << 0x10;
    }
    puVar6 = (uint *)FUN_ffff0000be056494
                               (param_1,uVar3,s_scp_cache_dump_buf_size_ffff0000be0c1291,&local_24);
    if (puVar6 == (uint *)0x0) {
      DAT_ffff0000be132628 = 0;
    }
    else {
      uVar1 = (*puVar6 & 0xff00ff00) >> 8 | (*puVar6 & 0xff00ff) << 8;
      DAT_ffff0000be132628 = uVar1 >> 0x10 | uVar1 << 0x10;
    }
    puVar6 = (uint *)FUN_ffff0000be056494
                               (param_1,uVar3,s_scp_reg_dump_buf_size_ffff0000be0bc712,&local_24);
    if (puVar6 == (uint *)0x0) {
      DAT_ffff0000be13262c = 0;
    }
    else {
      uVar1 = (*puVar6 & 0xff00ff00) >> 8 | (*puVar6 & 0xff00ff) << 8;
      DAT_ffff0000be13262c = uVar1 >> 0x10 | uVar1 << 0x10;
    }
    puVar6 = (uint *)FUN_ffff0000be056494
                               (param_1,uVar3,s_scp_tbuf_dump_buf_size_ffff0000be0c59e3,&local_24);
    if (puVar6 == (uint *)0x0) {
      DAT_ffff0000be132630 = 0;
    }
    else {
      uVar1 = (*puVar6 & 0xff00ff00) >> 8 | (*puVar6 & 0xff00ff) << 8;
      DAT_ffff0000be132630 = uVar1 >> 0x10 | uVar1 << 0x10;
    }
    lVar7 = FUN_ffff0000be056494(param_1,uVar3,s_status_ffff0000be0b92bf,&local_24);
    if (lVar7 == 0) {
      DAT_ffff0000be13260c = 0;
    }
    else {
      iVar4 = FUN_ffff0000be060f54(lVar7,&DAT_ffff0000be0aef74,(long)local_24);
      DAT_ffff0000be13260c = (uint)(iVar4 == 0);
    }
    lVar7 = FUN_ffff0000be056494(param_1,uVar3,s_scpctl_ffff0000be0ba5a2,&local_24);
    uVar5 = 0;
    if (lVar7 != 0) {
      uVar5 = FUN_ffff0000be05f4f8();
    }
    DAT_ffff0000be132610 = uVar5;
    DAT_ffff0000be132614 = '\0';
    lVar7 = FUN_ffff0000be056494(param_1,uVar3,s_secure_dump_ffff0000be0b26ed,&local_24);
    if (((lVar7 != 0) && (0 < local_24)) &&
       (iVar4 = FUN_ffff0000be060f54(lVar7,s_enable_ffff0000be0aef79), iVar4 == 0)) {
      DAT_ffff0000be132614 = '\x01';
    }
    DAT_ffff0000be132615 = DAT_ffff0000be132614;
    lVar7 = FUN_ffff0000be056494(param_1,uVar3,s_scp_protect_ffff0000be0b26f9,&local_24);
    if ((lVar7 == 0) || (local_24 < 1)) {
      bVar2 = DAT_ffff0000be132615 != '\0';
    }
    else {
      iVar4 = FUN_ffff0000be060f54(lVar7,s_disable_ffff0000be0adedb);
      if (iVar4 == 0) {
        DAT_ffff0000be132615 = 0;
        return;
      }
      bVar2 = true;
      DAT_ffff0000be132615 = '\x01';
    }
    if ((DAT_ffff0000be132614 == '\0') && (bVar2)) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff0000be05ee98
                (s_platform_mediatek_common_scp_scp_ffff0000be0b26ca,0x15a,
                 s__scp_configs_secure_dump______sc_ffff0000be0b0506);
    }
  }
  return;
}


