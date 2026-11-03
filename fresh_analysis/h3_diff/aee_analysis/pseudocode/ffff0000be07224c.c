
undefined4 FUN_ffff0000be07224c(void)

{
  int iVar1;
  undefined8 uVar2;
  ulong local_d0 [18];
  undefined8 uStack_40;
  undefined1 auStack_3c [4];
  undefined2 local_38;
  
  local_38 = 0;
  local_d0[0x11] = 0;
  uStack_40 = 0;
  local_d0[0xf] = 0;
  local_d0[0x10] = 0;
  local_d0[0xd] = 0;
  local_d0[0xe] = 0;
  local_d0[0xb] = 0;
  local_d0[0xc] = 0;
  local_d0[9] = 0;
  local_d0[10] = 0;
  local_d0[7] = 0;
  local_d0[8] = 0;
  local_d0[5] = 0;
  local_d0[6] = 0;
  local_d0[3] = 0;
  local_d0[4] = 0;
  local_d0[1] = 0;
  local_d0[2] = 0;
  local_d0[0] = 0;
  iVar1 = FUN_ffff0000be0721b0(local_d0);
  if (iVar1 == 0) {
    FUN_ffff0000be05fd98
              (s__ufs_id_0x_x_pn__s_rev__s__ffff0000be0bdb52,local_d0[0] & 0xffff,
               (ulong)local_d0 | 2,auStack_3c);
    if ((short)local_d0[0] == DAT_ffff0000be0f2da0) {
      uVar2 = FUN_ffff0000be060e5c(s_MT256GBEAX4U4001_ffff0000be0f2da2);
      iVar1 = FUN_ffff0000be060f54((ulong)local_d0 | 2,s_MT256GBEAX4U4001_ffff0000be0f2da2,uVar2);
      if (iVar1 == 0) {
        uVar2 = FUN_ffff0000be060e5c(&DAT_ffff0000be0f2db3);
        iVar1 = FUN_ffff0000be060f54(auStack_3c,&DAT_ffff0000be0f2db3,uVar2);
        if (iVar1 == 0) {
          FUN_ffff0000be05fd98
                    (s__ufs__get_disable_eua_size_polic_ffff0000be0b93e8,0,
                     CONCAT44(uRamffff0000be0f2dbc,DAT_ffff0000be0f2db8));
          return DAT_ffff0000be0f2db8;
        }
      }
    }
  }
  else {
    FUN_ffff0000be05fd98(s__ufs_unique_id_get_failed____ffff0000be0bdb34);
  }
  return 0;
}


