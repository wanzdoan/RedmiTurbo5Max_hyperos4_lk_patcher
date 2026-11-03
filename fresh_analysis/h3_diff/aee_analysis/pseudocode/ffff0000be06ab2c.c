
undefined8 FUN_ffff0000be06ab2c(long param_1)

{
  long lVar1;
  int iVar2;
  undefined8 uVar3;
  undefined8 uVar4;
  long local_28;
  
  local_28 = 0;
  iVar2 = FUN_ffff0000be05db54
                    (PTR_DAT_ffff0000be0f5570,s_USB_uncached_memory_ffff0000be0a882d,0x40,&local_28,
                     0xc,0,1);
  lVar1 = local_28;
  if (iVar2 == 0) {
    FUN_ffff0000be060ae0(local_28,0,0x40);
    uVar4 = FUN_ffff0000be05d228(lVar1);
    uVar3 = 0;
    *(long *)(param_1 + 0x20) = lVar1;
    *(long *)(param_1 + 0x28) = lVar1;
    *(undefined8 *)(param_1 + 8) = uVar4;
    *(long *)(param_1 + 0x10) = lVar1;
    *(long *)(param_1 + 0x18) = lVar1 + 0x30;
  }
  else {
    uVar3 = 0xfffffff4;
  }
  return uVar3;
}


