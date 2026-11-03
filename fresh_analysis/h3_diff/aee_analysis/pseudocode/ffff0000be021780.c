
undefined8 FUN_ffff0000be021780(long param_1)

{
  uint uVar1;
  int iVar2;
  undefined8 *puVar3;
  long lVar4;
  undefined8 *puVar5;
  int local_24;
  
  if (param_1 == 0) {
    return 0xffffffff;
  }
  FUN_ffff0000be020ec8(&DAT_ffff0000be1179c0);
  puVar3 = (undefined8 *)FUN_ffff0000be05f1bc(0x20);
  if (puVar3 != (undefined8 *)0x0) {
    local_24 = 0;
    iVar2 = FUN_ffff0000be05620c(param_1,s__soc_emicen_ffff0000be0c2b0e);
    if ((((iVar2 < 0) &&
         (iVar2 = FUN_ffff0000be05620c(param_1,s__emicen_ffff0000be0c3f10), iVar2 < 0)) &&
        (iVar2 = FUN_ffff0000be05620c(param_1,0), iVar2 < 0)) ||
       ((lVar4 = FUN_ffff0000be056494(param_1,iVar2,&DAT_ffff0000be0ae6fc,&local_24), lVar4 == 0 ||
        (local_24 == 0)))) {
      if (puVar3[3] != 0) {
        thunk_FUN_ffff0000be09937c();
      }
    }
    else {
      uVar1 = local_24 >> 4;
      puVar5 = (undefined8 *)FUN_ffff0000be05f1bc((ulong)uVar1 << 3);
      puVar3[3] = puVar5;
      if (puVar5 != (undefined8 *)0x0) {
        *puVar5 = 0xffff00001046e000;
        puVar5[1] = 0xffff00001056e000;
        *puVar3 = 0x112345678;
        *(undefined4 *)(puVar3 + 1) = 3;
        *(uint *)((long)puVar3 + 0xc) = uVar1;
        puVar3[2] = 0x1234567800000081;
        DAT_ffff0000be117a70 = puVar3;
        return 0;
      }
    }
    thunk_FUN_ffff0000be09937c(puVar3);
    return 0xffffffff;
  }
  return 0xffffffff;
}


