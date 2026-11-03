
bool FUN_ffff0000be029e30(undefined8 param_1)

{
  uint uVar1;
  int iVar2;
  uint *puVar3;
  undefined4 local_24;
  
  local_24 = 0;
  iVar2 = FUN_ffff0000be056824(param_1,0xffffffff,s_mediatek_scp_ffff0000be0b40da);
  if (iVar2 < 0) {
    FUN_ffff0000be05fd98(s__SCP__get_node_fail__nodeoffset___ffff0000be0b6544,iVar2);
  }
  puVar3 = (uint *)FUN_ffff0000be056494(param_1,iVar2,s_memorydump_ffff0000be0c1b90,&local_24);
  if (puVar3 == (uint *)0x0) {
    FUN_ffff0000be05fd98
              (s__SCP___s_Cannot_get_memorydump_s_ffff0000be0b2f2d,
               s_scp_memorydump_size_probe_ffff0000be0c62c1);
  }
  else {
    uVar1 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
    DAT_ffff0000be11816c = DAT_ffff0000be118168 + (uVar1 >> 0x10 | uVar1 << 0x10);
    uVar1 = (puVar3[1] & 0xff00ff00) >> 8 | (puVar3[1] & 0xff00ff) << 8;
    DAT_ffff0000be118170 = DAT_ffff0000be11816c + (uVar1 >> 0x10 | uVar1 << 0x10);
    uVar1 = (puVar3[2] & 0xff00ff00) >> 8 | (puVar3[2] & 0xff00ff) << 8;
    DAT_ffff0000be118174 = DAT_ffff0000be118170 + (uVar1 >> 0x10 | uVar1 << 0x10);
    uVar1 = (puVar3[3] & 0xff00ff00) >> 8 | (puVar3[3] & 0xff00ff) << 8;
    DAT_ffff0000be118178 = DAT_ffff0000be118174 + (uVar1 >> 0x10 | uVar1 << 0x10);
    uVar1 = (puVar3[4] & 0xff00ff00) >> 8 | (puVar3[4] & 0xff00ff) << 8;
    DAT_ffff0000be11817c = DAT_ffff0000be118178 + (uVar1 >> 0x10 | uVar1 << 0x10);
  }
  return puVar3 != (uint *)0x0;
}


