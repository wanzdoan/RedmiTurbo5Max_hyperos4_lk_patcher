// ffff000090720b40  size=984  FUN_ffff000090720b40

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined4 FUN_ffff000090720b40(undefined8 param_1)

{
  long lVar1;
  uint uVar2;
  byte bVar3;
  byte bVar4;
  undefined2 uVar5;
  int iVar6;
  long lVar7;
  uint local_10c [40];
  uint local_6c [3];
  
  FUN_ffff00009077e11c(&DAT_ffff0000907f0ca5,s_mt6379_2p_init_setting_ffff0000907e5e9f);
  FUN_ffff00009077e11c(s__s__start_ffff0000907e90c2,s_mt6379_2p_parse_dt_ffff0000907da893);
  iVar6 = FUN_ffff00009076e32c(param_1,0xffffffff,s_mediatek_mt6379_2p_master_ffff0000907ec235);
  if (iVar6 < 0) {
    FUN_ffff00009077e11c(s_failed_to_get_offset__d__ffff0000907eddff,iVar6);
    FUN_ffff00009077e11c(s_parse_dt_failed__d__ffff0000907ef615,0xffffffff);
    return 0xffffffff;
  }
  local_10c[0] = 0;
  lVar7 = FUN_ffff00009076df9c(param_1,iVar6,s_disable_cs_ffff0000907e5eb6,local_10c);
  _DAT_ffff000090860650 = (uint)(lVar7 != 0);
  local_10c[0] = 0;
  FUN_ffff00009076df9c(param_1,iVar6,s_cs_gpios_ffff0000907e5e7e,local_10c);
  _DAT_ffff000090860670 = s_mt6379_2p_master_ffff0000907fcf51;
  if (_DAT_ffff000090860650 != 0) {
    return 0xffffffff;
  }
  FUN_ffff00009077e11c(&DAT_ffff0000907da88f,0);
  local_10c[0] = 7;
  local_10c[2] = 2;
  local_10c[4] = 3;
  local_10c[6] = 6;
  local_10c[8] = 10;
  local_10c[10] = 0xd;
  local_10c[0xc] = 0x12;
  local_10c[0xe] = 0x14;
  local_10c[0x10] = 0x15;
  local_10c[0x12] = 0x16;
  local_10c[0x14] = 0x17;
  local_10c[0x16] = 0x18;
  local_10c[0x18] = 0x19;
  local_10c[0x1a] = 0x1a;
  local_10c[0x1c] = 0x1b;
  local_10c[0x1e] = 0x1c;
  local_10c[0x20] = 0x1d;
  local_10c[0x22] = 0x1e;
  local_10c[0x24] = 0x20;
  local_10c[0x26] = 0x21;
  local_6c[0] = 0;
  iVar6 = FUN_ffff000090737554(_DAT_ffff000090860648,4,local_6c);
  if (iVar6 < 0) {
    iVar6 = -1;
LAB_ffff000090720d30:
    FUN_ffff00009077e11c(s_mt6379_2p_write_field__d_fail____ffff0000907dc4c4,0xc,iVar6);
    FUN_ffff00009077e11c
              (s__s_Failed_to_reset_registers__d__ffff0000907fed7a,
               s_mt6379_2p_init_device_ffff0000907f108d,iVar6);
  }
  else {
    local_6c[0] = local_6c[0] & 1;
    iVar6 = FUN_ffff00009073751c(_DAT_ffff000090860648,4);
    if (iVar6 < 0) goto LAB_ffff000090720d30;
  }
  lVar7 = 0;
  do {
    uVar2 = *(uint *)((long)local_10c + lVar7);
    local_6c[0] = 0;
    lVar1 = (ulong)uVar2 * 4;
    uVar5 = *(undefined2 *)(lVar1 + -0xffff6f7fb6b8);
    bVar3 = *(byte *)(lVar1 + -0xffff6f7fb6b6);
    bVar4 = *(byte *)(lVar1 + -0xffff6f7fb6b5);
    iVar6 = FUN_ffff000090737554(_DAT_ffff000090860648,uVar5,local_6c);
    if (iVar6 < 0) {
      iVar6 = -1;
LAB_ffff000090720d7c:
      FUN_ffff00009077e11c(s_mt6379_2p_write_field__d_fail____ffff0000907dc4c4,(ulong)uVar2,iVar6);
    }
    else {
      local_6c[0] = 0xff << (ulong)(7 - bVar4 & 0x1f) & 0xffU >> (ulong)(bVar3 & 0x1f) & local_6c[0]
      ;
      iVar6 = FUN_ffff00009073751c(_DAT_ffff000090860648,uVar5);
      if (iVar6 < 0) goto LAB_ffff000090720d7c;
    }
    lVar7 = lVar7 + 8;
  } while (lVar7 != 0xa0);
  iVar6 = 0;
  local_6c[0] = 0;
  do {
    FUN_ffff000090737554(_DAT_ffff000090860648,iVar6,local_6c);
    FUN_ffff00009077e11c
              (s__s_reg_0x_02x____0x_02x_ffff0000907e90a9,
               s_mt6379_2p_dump_registers_ffff0000907f7f12,iVar6,local_6c[0]);
    iVar6 = iVar6 + 1;
  } while (iVar6 != 0x21);
  local_10c[0] = 0;
  iVar6 = FUN_ffff000090737554(_DAT_ffff000090860648,4,local_10c);
  if (iVar6 < 0) {
    iVar6 = -1;
  }
  else {
    local_10c[0] = local_10c[0] & 0x10;
    iVar6 = FUN_ffff00009073751c(_DAT_ffff000090860648,4);
    if (-1 < iVar6) goto LAB_ffff000090720e90;
  }
  FUN_ffff00009077e11c(s_mt6379_2p_write_field__d_fail____ffff0000907dc4c4,0xf,iVar6);
LAB_ffff000090720e90:
  local_10c[0] = 0;
  iVar6 = FUN_ffff000090737554(_DAT_ffff000090860648,0x15,local_10c);
  if (iVar6 < 0) {
    iVar6 = -1;
  }
  else {
    local_10c[0] = local_10c[0] & 1;
    iVar6 = FUN_ffff00009073751c(_DAT_ffff000090860648,0x15);
    if (-1 < iVar6) {
      DAT_ffff000090860678 = 1;
      return 0;
    }
  }
  FUN_ffff00009077e11c(s_mt6379_2p_write_field__d_fail____ffff0000907dc4c4,0x2a,iVar6);
  DAT_ffff000090860678 = 1;
  return 0;
}


