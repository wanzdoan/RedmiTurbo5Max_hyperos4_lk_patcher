// ffff00009078a648  size=668  FUN_ffff00009078a648

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009078a648(long param_1)

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
    uVar3 = FUN_ffff00009076e064(param_1,0xffffffff,s_mediatek_scp_ffff0000907e85fa);
    puVar6 = (uint *)FUN_ffff00009076dcd4(param_1,uVar3,s_scp_sram_size_ffff0000907f7254,&local_24);
    if (puVar6 == (uint *)0x0) {
      _DAT_ffff000090887708 = 0;
    }
    else {
      uVar1 = (*puVar6 & 0xff00ff00) >> 8 | (*puVar6 & 0xff00ff) << 8;
      _DAT_ffff000090887708 = uVar1 >> 0x10 | uVar1 << 0x10;
    }
    puVar6 = (uint *)FUN_ffff00009076dcd4(param_1,uVar3,s_scp_dram_size_ffff0000907fc542,&local_24);
    if (puVar6 == (uint *)0x0) {
      _DAT_ffff000090887720 = 0;
    }
    else {
      uVar1 = (*puVar6 & 0xff00ff00) >> 8 | (*puVar6 & 0xff00ff) << 8;
      _DAT_ffff000090887720 = uVar1 >> 0x10 | uVar1 << 0x10;
    }
    puVar6 = (uint *)FUN_ffff00009076dcd4
                               (param_1,uVar3,s_scp_cache_dump_buf_size_ffff0000907fa7f8,&local_24);
    if (puVar6 == (uint *)0x0) {
      _DAT_ffff000090887728 = 0;
    }
    else {
      uVar1 = (*puVar6 & 0xff00ff00) >> 8 | (*puVar6 & 0xff00ff) << 8;
      _DAT_ffff000090887728 = uVar1 >> 0x10 | uVar1 << 0x10;
    }
    puVar6 = (uint *)FUN_ffff00009076dcd4
                               (param_1,uVar3,s_scp_reg_dump_buf_size_ffff0000907f3714,&local_24);
    if (puVar6 == (uint *)0x0) {
      _DAT_ffff00009088772c = 0;
    }
    else {
      uVar1 = (*puVar6 & 0xff00ff00) >> 8 | (*puVar6 & 0xff00ff) << 8;
      _DAT_ffff00009088772c = uVar1 >> 0x10 | uVar1 << 0x10;
    }
    puVar6 = (uint *)FUN_ffff00009076dcd4
                               (param_1,uVar3,s_scp_tbuf_dump_buf_size_ffff0000908016b9,&local_24);
    if (puVar6 == (uint *)0x0) {
      _DAT_ffff000090887730 = 0;
    }
    else {
      uVar1 = (*puVar6 & 0xff00ff00) >> 8 | (*puVar6 & 0xff00ff) << 8;
      _DAT_ffff000090887730 = uVar1 >> 0x10 | uVar1 << 0x10;
    }
    lVar7 = FUN_ffff00009076dcd4(param_1,uVar3,s_status_ffff0000907edb6a,&local_24);
    if (lVar7 == 0) {
      _DAT_ffff00009088770c = 0;
    }
    else {
      iVar4 = FUN_ffff00009077f080(lVar7,&DAT_ffff0000907df5a6,(long)local_24);
      _DAT_ffff00009088770c = (uint)(iVar4 == 0);
    }
    lVar7 = FUN_ffff00009076dcd4(param_1,uVar3,s_scpctl_ffff0000907f05c7,&local_24);
    uVar5 = 0;
    if (lVar7 != 0) {
      uVar5 = FUN_ffff00009077d588();
    }
    _DAT_ffff000090887710 = uVar5;
    DAT_ffff000090887714 = '\0';
    lVar7 = FUN_ffff00009076dcd4(param_1,uVar3,s_secure_dump_ffff0000907e54e7,&local_24);
    if (((lVar7 != 0) && (0 < local_24)) &&
       (iVar4 = FUN_ffff00009077f080(lVar7,s_enable_ffff0000907e0358), iVar4 == 0)) {
      DAT_ffff000090887714 = '\x01';
    }
    DAT_ffff000090887715 = DAT_ffff000090887714;
    lVar7 = FUN_ffff00009076dcd4(param_1,uVar3,s_scp_protect_ffff0000907e54f3,&local_24);
    if ((lVar7 == 0) || (local_24 < 1)) {
      bVar2 = DAT_ffff000090887715 != '\0';
    }
    else {
      iVar4 = FUN_ffff00009077f080(lVar7,s_disable_ffff0000907dec82);
      if (iVar4 == 0) {
        DAT_ffff000090887715 = 0;
        return;
      }
      bVar2 = true;
      DAT_ffff000090887715 = '\x01';
    }
    if ((DAT_ffff000090887714 == '\0') && (bVar2)) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cd04
                (s_platform_mediatek_common_scp_scp_ffff0000907e54c4,0x15a,
                 s__scp_configs_secure_dump______sc_ffff0000907e1fbb);
    }
  }
  return;
}


