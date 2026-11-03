// ffff0000907209b8  size=984  FUN_ffff0000907209b8

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined4 FUN_ffff0000907209b8(undefined8 param_1)

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
  
  FUN_ffff00009077de54(&DAT_ffff0000907f0b1d,s_mt6379_2p_init_setting_ffff0000907e5d74);
  FUN_ffff00009077de54(s__s__start_ffff0000907e8f67,s_mt6379_2p_parse_dt_ffff0000907da771);
  iVar6 = FUN_ffff00009076e064(param_1,0xffffffff,s_mediatek_mt6379_2p_master_ffff0000907ec09b);
  if (iVar6 < 0) {
    FUN_ffff00009077de54(s_failed_to_get_offset__d__ffff0000907edc77,iVar6);
    FUN_ffff00009077de54(s_parse_dt_failed__d__ffff0000907ef48d,0xffffffff);
    return 0xffffffff;
  }
  local_10c[0] = 0;
  lVar7 = FUN_ffff00009076dcd4(param_1,iVar6,s_disable_cs_ffff0000907e5d8b,local_10c);
  _DAT_ffff0000908605b8 = (uint)(lVar7 != 0);
  local_10c[0] = 0;
  FUN_ffff00009076dcd4(param_1,iVar6,s_cs_gpios_ffff0000907e5d53,local_10c);
  _DAT_ffff0000908605d8 = s_mt6379_2p_master_ffff0000907fcda9;
  if (_DAT_ffff0000908605b8 != 0) {
    return 0xffffffff;
  }
  FUN_ffff00009077de54(&DAT_ffff0000907da76d,0);
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
  iVar6 = FUN_ffff0000907372bc(_DAT_ffff0000908605b0,4,local_6c);
  if (iVar6 < 0) {
    iVar6 = -1;
LAB_ffff000090720ba8:
    FUN_ffff00009077de54(s_mt6379_2p_write_field__d_fail____ffff0000907dc3d7,0xc,iVar6);
    FUN_ffff00009077de54
              (s__s_Failed_to_reset_registers__d__ffff0000907febd2,
               s_mt6379_2p_init_device_ffff0000907f0f05,iVar6);
  }
  else {
    local_6c[0] = local_6c[0] & 1;
    iVar6 = FUN_ffff000090737284(_DAT_ffff0000908605b0,4);
    if (iVar6 < 0) goto LAB_ffff000090720ba8;
  }
  lVar7 = 0;
  do {
    uVar2 = *(uint *)((long)local_10c + lVar7);
    local_6c[0] = 0;
    lVar1 = (ulong)uVar2 * 4;
    uVar5 = *(undefined2 *)(lVar1 + -0xffff6f7fb8e0);
    bVar3 = *(byte *)(lVar1 + -0xffff6f7fb8de);
    bVar4 = *(byte *)(lVar1 + -0xffff6f7fb8dd);
    iVar6 = FUN_ffff0000907372bc(_DAT_ffff0000908605b0,uVar5,local_6c);
    if (iVar6 < 0) {
      iVar6 = -1;
LAB_ffff000090720bf4:
      FUN_ffff00009077de54(s_mt6379_2p_write_field__d_fail____ffff0000907dc3d7,(ulong)uVar2,iVar6);
    }
    else {
      local_6c[0] = 0xff << (ulong)(7 - bVar4 & 0x1f) & 0xffU >> (ulong)(bVar3 & 0x1f) & local_6c[0]
      ;
      iVar6 = FUN_ffff000090737284(_DAT_ffff0000908605b0,uVar5);
      if (iVar6 < 0) goto LAB_ffff000090720bf4;
    }
    lVar7 = lVar7 + 8;
  } while (lVar7 != 0xa0);
  iVar6 = 0;
  local_6c[0] = 0;
  do {
    FUN_ffff0000907372bc(_DAT_ffff0000908605b0,iVar6,local_6c);
    FUN_ffff00009077de54
              (s__s_reg_0x_02x____0x_02x_ffff0000907e8f4e,
               s_mt6379_2p_dump_registers_ffff0000907f7d76,iVar6,local_6c[0]);
    iVar6 = iVar6 + 1;
  } while (iVar6 != 0x21);
  local_10c[0] = 0;
  iVar6 = FUN_ffff0000907372bc(_DAT_ffff0000908605b0,4,local_10c);
  if (iVar6 < 0) {
    iVar6 = -1;
  }
  else {
    local_10c[0] = local_10c[0] & 0x10;
    iVar6 = FUN_ffff000090737284(_DAT_ffff0000908605b0,4);
    if (-1 < iVar6) goto LAB_ffff000090720d08;
  }
  FUN_ffff00009077de54(s_mt6379_2p_write_field__d_fail____ffff0000907dc3d7,0xf,iVar6);
LAB_ffff000090720d08:
  local_10c[0] = 0;
  iVar6 = FUN_ffff0000907372bc(_DAT_ffff0000908605b0,0x15,local_10c);
  if (iVar6 < 0) {
    iVar6 = -1;
  }
  else {
    local_10c[0] = local_10c[0] & 1;
    iVar6 = FUN_ffff000090737284(_DAT_ffff0000908605b0,0x15);
    if (-1 < iVar6) {
      DAT_ffff0000908605e0 = 1;
      return 0;
    }
  }
  FUN_ffff00009077de54(s_mt6379_2p_write_field__d_fail____ffff0000907dc3d7,0x2a,iVar6);
  DAT_ffff0000908605e0 = 1;
  return 0;
}


