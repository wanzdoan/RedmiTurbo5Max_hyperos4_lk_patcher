
undefined4 FUN_ffff0000907948c8(void)

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
  iVar1 = FUN_ffff00009079482c(local_d0);
  if (iVar1 == 0) {
    FUN_ffff00009077e11c
              (s__ufs_id_0x_x_pn__s_rev__s__ffff0000907f578b,local_d0[0] & 0xffff,
               (ulong)local_d0 | 2,auStack_3c);
    if ((short)local_d0[0] == DAT_ffff0000908367a8) {
      uVar2 = FUN_ffff00009077f250(s_MT256GBEAX4U4001_ffff0000908367aa);
      iVar1 = FUN_ffff00009077f348((ulong)local_d0 | 2,s_MT256GBEAX4U4001_ffff0000908367aa,uVar2);
      if (iVar1 == 0) {
        uVar2 = FUN_ffff00009077f250(&DAT_ffff0000908367bb);
        iVar1 = FUN_ffff00009077f348(auStack_3c,&DAT_ffff0000908367bb,uVar2);
        if (iVar1 == 0) {
          FUN_ffff00009077e11c
                    (s__ufs__get_disable_eua_size_polic_ffff0000907ef02a,0,
                     CONCAT44(uRamffff0000908367c4,DAT_ffff0000908367c0));
          return DAT_ffff0000908367c0;
        }
      }
    }
  }
  else {
    FUN_ffff00009077e11c(s__ufs_unique_id_get_failed____ffff0000907f576d);
  }
  return 0;
}


