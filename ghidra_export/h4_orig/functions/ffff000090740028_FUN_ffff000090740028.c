// ffff000090740028  size=372  FUN_ffff000090740028

int FUN_ffff000090740028(undefined8 param_1,int param_2)

{
  uint *puVar1;
  int iVar2;
  int iVar3;
  uint *puVar4;
  uint *puVar5;
  undefined8 local_78;
  undefined8 uStack_70;
  ushort local_64 [2];
  
  local_78 = 0x77666d7073;
  uStack_70 = 0;
  puVar4 = (uint *)FUN_ffff00009077d514(0x10000);
  if (puVar4 == (uint *)0x0) {
    FUN_ffff00009077e11c(s__spmfw__aee__malloc_fail_ffff0000907d2ea1);
    iVar2 = -1;
  }
  else {
    iVar2 = FUN_ffff000090785d18(&local_78,s_spmfw_ffff0000907f167b,puVar4,0x10000);
    if (iVar2 < 0) {
      FUN_ffff00009077e11c(s__spmfw__aee__load_spmfw_fail__re_ffff0000907ff342,iVar2);
    }
    else {
      FUN_ffff000090707c70(puVar4,0x10000);
      if (*puVar4 >> 1 == 0x29a82699) {
        puVar5 = puVar4;
        do {
          puVar1 = puVar5 + 0x10;
          local_64[0] = 0;
          FUN_ffff00009077eb20(local_64,puVar1,2);
          iVar3 = FUN_ffff00009077f6fc
                            (param_1,(long)param_2,&DAT_ffff0000907dbf7c,
                             puVar1 + (ulong)local_64[0] + 9);
          if (iVar3 < 0) {
            FUN_ffff00009077e11c(s_copy_spmfw_version_string_fail_ffff0000907d47eb);
          }
          else {
            FUN_ffff00009077e11c(s_spmfw_version___s_ffff0000907ee449,param_1);
          }
          puVar5 = (uint *)((long)puVar1 + (ulong)puVar5[1]);
        } while ((*puVar5 & 0xfffffffe) == 0x53504d32);
      }
    }
    thunk_FUN_ffff0000907c314c(puVar4);
  }
  return iVar2;
}


