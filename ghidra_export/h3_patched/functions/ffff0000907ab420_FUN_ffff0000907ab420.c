// ffff0000907ab420  size=428  FUN_ffff0000907ab420

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

ulong FUN_ffff0000907ab420(undefined4 param_1,undefined4 param_2,undefined4 param_3)

{
  undefined1 uVar1;
  int iVar2;
  uint uVar3;
  int iVar4;
  ulong local_118;
  undefined8 uStack_110;
  undefined8 local_108;
  undefined8 uStack_100;
  undefined8 local_f8;
  undefined8 uStack_f0;
  undefined8 local_e8;
  undefined8 uStack_e0;
  undefined8 local_d8;
  ulong local_d0;
  undefined8 local_c8;
  undefined8 local_c0;
  undefined8 local_b8;
  undefined8 local_b0;
  undefined8 local_a8;
  undefined8 local_a0;
  undefined8 local_98;
  undefined8 local_90;
  undefined8 local_88;
  undefined8 local_80;
  undefined8 uStack_78;
  undefined8 local_70;
  undefined8 uStack_68;
  undefined8 local_60;
  undefined8 uStack_58;
  undefined8 local_50;
  undefined8 uStack_48;
  
  local_50 = 0;
  uStack_48 = 0;
  local_60 = 0;
  uStack_58 = 0;
  local_70 = 0;
  uStack_68 = 0;
  local_80 = 0;
  uStack_78 = 0;
  local_88 = 0;
  local_98 = 0;
  local_90 = 0;
  local_a8 = 0;
  local_a0 = 0;
  local_b8 = 0;
  local_b0 = 0;
  local_c8 = 0;
  local_c0 = 0;
  local_d0 = 0;
  FUN_ffff00009077eb88(&local_88,0,0x48);
  FUN_ffff00009077eb88(&local_d0,0,0x48);
  _DAT_ffff0000180100c0 = 0xffffffff;
  _DAT_ffff000018010030 = 3;
  while ((_DAT_ffff000018010050 & 1) != 0) {
    _DAT_ffff000018010060 = 1;
  }
  while ((_DAT_ffff000018010080 & 1) != 0) {
    _DAT_ffff000018010090 = 1;
  }
  iVar2 = -1000000;
  do {
    iVar4 = iVar2;
    if ((_DAT_ffff000018010020 & 1) != 0) break;
    iVar2 = iVar4 + 1;
  } while (iVar4 != 0);
  if (iVar4 == 0) {
    FUN_ffff00009077de54
              (s__s__poll_mailbox_for_hwrot_faile_ffff0000907da14d,
               s_hwrot_mailbox_init_ffff0000907e05ff);
    uVar3 = 4;
  }
  else {
    uVar3 = _DAT_ffff0000180100b0 & 0xff;
  }
  FUN_ffff00009077de54(s_Mailbox_init_status_0x_x__ffff0000907e6f2e,uVar3);
  uVar1 = (undefined1)local_80;
  local_88 = CONCAT26(local_88._6_2_,0x11400000004);
  local_80 = CONCAT44(param_1,(undefined4)local_80);
  uStack_78 = CONCAT44(param_2,0x100);
  local_70 = CONCAT44(param_3,0x100);
  local_80 = CONCAT71(local_80._1_7_,uVar1) & 0xfffffffffffffff0 | 5;
  FUN_ffff0000907ab180(&local_118,&local_88);
  local_a0 = local_e8;
  local_98 = uStack_e0;
  local_90 = local_d8;
  local_c0 = local_108;
  local_b8 = uStack_100;
  local_b0 = local_f8;
  local_a8 = uStack_f0;
  local_d0 = local_118;
  local_c8 = uStack_110;
  if ((int)local_118 == 0) {
    local_d0 = 0;
  }
  else {
    FUN_ffff00009077de54(s_Send_mbx_rsa_fail__err___0x_x__ffff0000907d21e3);
    local_d0 = local_d0 & 0xffffffff;
  }
  return local_d0;
}


