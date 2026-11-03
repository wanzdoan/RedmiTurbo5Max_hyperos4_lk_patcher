// ffff00009073fd90  size=372  FUN_ffff00009073fd90

int FUN_ffff00009073fd90(undefined8 param_1,int param_2)

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
  puVar4 = (uint *)FUN_ffff00009077d24c(0x10000);
  if (puVar4 == (uint *)0x0) {
    FUN_ffff00009077de54(s__spmfw__aee__malloc_fail_ffff0000907d2e64);
    iVar2 = -1;
  }
  else {
    iVar2 = FUN_ffff000090785a50(&local_78,s_spmfw_ffff0000907f14f3,puVar4,0x10000);
    if (iVar2 < 0) {
      FUN_ffff00009077de54(s__spmfw__aee__load_spmfw_fail__re_ffff0000907ff19a,iVar2);
    }
    else {
      FUN_ffff000090707ce4(puVar4,0x10000);
      if (*puVar4 >> 1 == 0x29a82699) {
        puVar5 = puVar4;
        do {
          puVar1 = puVar5 + 0x10;
          local_64[0] = 0;
          FUN_ffff00009077e858(local_64,puVar1,2);
          iVar3 = FUN_ffff00009077f434
                            (param_1,(long)param_2,&DAT_ffff0000907dbe5a,
                             puVar1 + (ulong)local_64[0] + 9);
          if (iVar3 < 0) {
            FUN_ffff00009077de54(s_copy_spmfw_version_string_fail_ffff0000907d47ae);
          }
          else {
            FUN_ffff00009077de54(s_spmfw_version___s_ffff0000907ee2c1,param_1);
          }
          puVar5 = (uint *)((long)puVar1 + (ulong)puVar5[1]);
        } while ((*puVar5 & 0xfffffffe) == 0x53504d32);
      }
    }
    thunk_FUN_ffff0000907c2ad8(puVar4);
  }
  return iVar2;
}


