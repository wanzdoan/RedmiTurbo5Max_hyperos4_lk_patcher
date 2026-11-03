
undefined8 * FUN_ffff0000be01181c(void)

{
  undefined8 *puVar1;
  long lVar2;
  
  puVar1 = (undefined8 *)FUN_ffff0000be05f1bc(0x30);
  if (puVar1 == (undefined8 *)0x0) {
    FUN_ffff0000be05fd98
              (s__s__d___s__malloc___failed__ffff0000be0c188c,
               s_mrdump_dev_emmc_ext4_ffff0000be0b98e0,0x5e,s_mrdump_dev_emmc_ext4_ffff0000be0b98e0)
    ;
  }
  else {
    lVar2 = FUN_ffff0000be05df6c(s_userdata_ffff0000be0b60cf);
    puVar1[2] = lVar2;
    if (lVar2 == 0) {
      FUN_ffff0000be05fd98
                (s__s__d__No_EXT4_partition_found__ffff0000be0b092a,
                 s_mrdump_dev_emmc_ext4_ffff0000be0b98e0,100);
      thunk_FUN_ffff0000be09937c(puVar1);
      puVar1 = (undefined8 *)0x0;
    }
    else {
      FUN_ffff0000be0114f0
                (s__s_size___jd_Mb_ffff0000be0b3dc7,s_userdata_ffff0000be0b60cf,
                 *(ulong *)(lVar2 + 0x20) >> 0x14);
      *puVar1 = &DAT_ffff0000be0b3dc2;
      puVar1[1] = s_userdata_ffff0000be0b60cf;
      puVar1[3] = FUN_ffff0000be0118e8;
      puVar1[4] = FUN_ffff0000be011984;
    }
  }
  return puVar1;
}


