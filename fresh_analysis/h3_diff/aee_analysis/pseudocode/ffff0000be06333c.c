
undefined4 FUN_ffff0000be06333c(void)

{
  int iVar1;
  long lVar2;
  undefined8 uVar3;
  undefined8 uVar4;
  undefined4 local_14;
  
  local_14 = 0;
  lVar2 = FUN_ffff0000be05df6c(s_expdb_ffff0000be0a79cf);
  if (lVar2 == 0) {
    FUN_ffff0000be063b40
              (s_kedump__Partition___s__is_not_ex_ffff0000be0a68a4,s_expdb_ffff0000be0a79cf);
    FUN_ffff0000be05fd98
              (s_kedump__Partition___s__is_not_ex_ffff0000be0a68a4,s_expdb_ffff0000be0a79cf);
    local_14 = 0;
  }
  else {
    uVar4 = *(undefined8 *)(lVar2 + 0x18);
    uVar3 = FUN_ffff0000be060e5c(s_nand0_ffff0000be0a7693);
    iVar1 = FUN_ffff0000be060f54(uVar4,s_nand0_ffff0000be0a7693,uVar3);
    if (iVar1 == 0) {
      FUN_ffff0000be05e154(*(undefined8 *)(lVar2 + 0xa8),0x100,&local_14);
    }
    else {
      local_14 = (undefined4)*(undefined8 *)(lVar2 + 0x28);
    }
    FUN_ffff0000be05def0(lVar2);
  }
  return local_14;
}


