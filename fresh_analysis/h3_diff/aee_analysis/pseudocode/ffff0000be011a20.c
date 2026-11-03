
undefined8 * FUN_ffff0000be011a20(long param_1)

{
  undefined8 *puVar1;
  long lVar2;
  
  puVar1 = (undefined8 *)FUN_ffff0000be05f1bc(0x30);
  if (puVar1 == (undefined8 *)0x0) {
    FUN_ffff0000be05fd98
              (s__s__d___s__malloc___failed__ffff0000be0c188c,
               s_mrdump_dev_partition_ffff0000be0b2bb9,0x75,s_mrdump_dev_partition_ffff0000be0b2bb9)
    ;
  }
  else {
    lVar2 = FUN_ffff0000be05df6c(param_1);
    puVar1[2] = lVar2;
    if ((lVar2 == 0) ||
       (FUN_ffff0000be0114f0
                  (s__s_size___jd_Mb_ffff0000be0b3dc7,param_1,*(ulong *)(lVar2 + 0x20) >> 0x14),
       param_1 == 0)) {
      FUN_ffff0000be05fd98
                (s__s__d__No_mrdump_partition_found_ffff0000be0a3159,
                 s_mrdump_dev_partition_ffff0000be0b2bb9,0x7b);
      thunk_FUN_ffff0000be09937c(puVar1);
      puVar1 = (undefined8 *)0x0;
    }
    else {
      *puVar1 = s_mpart_ffff0000be0b60d8;
      puVar1[1] = param_1;
      puVar1[3] = FUN_ffff0000be0118e8;
      puVar1[4] = FUN_ffff0000be011984;
    }
  }
  return puVar1;
}


