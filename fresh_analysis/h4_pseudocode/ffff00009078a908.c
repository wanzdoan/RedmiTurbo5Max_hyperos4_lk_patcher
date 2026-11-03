
void FUN_ffff00009078a908(long param_1)

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
    uVar3 = FUN_ffff00009076e32c(param_1,0xffffffff,s_mediatek_scp_ffff0000907e873e);
    puVar6 = (uint *)FUN_ffff00009076df9c(param_1,uVar3,s_scp_sram_size_ffff0000907f73de,&local_24);
    if (puVar6 == (uint *)0x0) {
      DAT_ffff0000908877a0 = 0;
    }
    else {
      uVar1 = (*puVar6 & 0xff00ff00) >> 8 | (*puVar6 & 0xff00ff) << 8;
      DAT_ffff0000908877a0 = uVar1 >> 0x10 | uVar1 << 0x10;
    }
    puVar6 = (uint *)FUN_ffff00009076df9c(param_1,uVar3,s_scp_dram_size_ffff0000907fc6de,&local_24);
    if (puVar6 == (uint *)0x0) {
      DAT_ffff0000908877b8 = 0;
    }
    else {
      uVar1 = (*puVar6 & 0xff00ff00) >> 8 | (*puVar6 & 0xff00ff) << 8;
      DAT_ffff0000908877b8 = uVar1 >> 0x10 | uVar1 << 0x10;
    }
    puVar6 = (uint *)FUN_ffff00009076df9c
                               (param_1,uVar3,s_scp_cache_dump_buf_size_ffff0000907fa994,&local_24);
    if (puVar6 == (uint *)0x0) {
      DAT_ffff0000908877c0 = 0;
    }
    else {
      uVar1 = (*puVar6 & 0xff00ff00) >> 8 | (*puVar6 & 0xff00ff) << 8;
      DAT_ffff0000908877c0 = uVar1 >> 0x10 | uVar1 << 0x10;
    }
    puVar6 = (uint *)FUN_ffff00009076df9c
                               (param_1,uVar3,s_scp_reg_dump_buf_size_ffff0000907f38a0,&local_24);
    if (puVar6 == (uint *)0x0) {
      DAT_ffff0000908877c4 = 0;
    }
    else {
      uVar1 = (*puVar6 & 0xff00ff00) >> 8 | (*puVar6 & 0xff00ff) << 8;
      DAT_ffff0000908877c4 = uVar1 >> 0x10 | uVar1 << 0x10;
    }
    puVar6 = (uint *)FUN_ffff00009076df9c
                               (param_1,uVar3,s_scp_tbuf_dump_buf_size_ffff000090801895,&local_24);
    if (puVar6 == (uint *)0x0) {
      DAT_ffff0000908877c8 = 0;
    }
    else {
      uVar1 = (*puVar6 & 0xff00ff00) >> 8 | (*puVar6 & 0xff00ff) << 8;
      DAT_ffff0000908877c8 = uVar1 >> 0x10 | uVar1 << 0x10;
    }
    lVar7 = FUN_ffff00009076df9c(param_1,uVar3,s_status_ffff0000907edcdb,&local_24);
    if (lVar7 == 0) {
      DAT_ffff0000908877a4 = 0;
    }
    else {
      iVar4 = FUN_ffff00009077f348(lVar7,&DAT_ffff0000907df649,(long)local_24);
      DAT_ffff0000908877a4 = (uint)(iVar4 == 0);
    }
    lVar7 = FUN_ffff00009076df9c(param_1,uVar3,s_scpctl_ffff0000907f0738,&local_24);
    uVar5 = 0;
    if (lVar7 != 0) {
      uVar5 = FUN_ffff00009077d850();
    }
    DAT_ffff0000908877a8 = uVar5;
    DAT_ffff0000908877ac = '\0';
    lVar7 = FUN_ffff00009076df9c(param_1,uVar3,s_secure_dump_ffff0000907e55eb,&local_24);
    if (((lVar7 != 0) && (0 < local_24)) &&
       (iVar4 = FUN_ffff00009077f348(lVar7,s_enable_ffff0000907e03fb), iVar4 == 0)) {
      DAT_ffff0000908877ac = '\x01';
    }
    DAT_ffff0000908877ad = DAT_ffff0000908877ac;
    lVar7 = FUN_ffff00009076df9c(param_1,uVar3,s_scp_protect_ffff0000907e55f7,&local_24);
    if ((lVar7 == 0) || (local_24 < 1)) {
      bVar2 = DAT_ffff0000908877ad != '\0';
    }
    else {
      iVar4 = FUN_ffff00009077f348(lVar7,s_disable_ffff0000907ded25);
      if (iVar4 == 0) {
        DAT_ffff0000908877ad = 0;
        return;
      }
      bVar2 = true;
      DAT_ffff0000908877ad = '\x01';
    }
    if ((DAT_ffff0000908877ac == '\0') && (bVar2)) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cfcc
                (s_platform_mediatek_common_scp_scp_ffff0000907e55c8,0x15a,
                 s__scp_configs_secure_dump______sc_ffff0000907e208c);
    }
  }
  return;
}


