
bool FUN_ffff0000be062f38(void)

{
  bool bVar1;
  int iVar2;
  long lVar3;
  undefined8 uVar4;
  undefined8 uVar5;
  
  if (DAT_ffff0000be1271a4 == 0) {
    lVar3 = FUN_ffff0000be05df6c(s_expdb_ffff0000be0a79cf);
    if (lVar3 == 0) {
      FUN_ffff0000be063b40
                (s_kedump__Partition___s__is_not_ex_ffff0000be0a68a4,s_expdb_ffff0000be0a79cf);
      FUN_ffff0000be05fd98
                (s_kedump__Partition___s__is_not_ex_ffff0000be0a68a4,s_expdb_ffff0000be0a79cf);
      bVar1 = false;
    }
    else {
      uVar5 = *(undefined8 *)(lVar3 + 0x18);
      uVar4 = FUN_ffff0000be060e5c(s_nand0_ffff0000be0a7693);
      iVar2 = FUN_ffff0000be060f54(uVar5,s_nand0_ffff0000be0a7693,uVar4);
      bVar1 = iVar2 == 0;
      if (iVar2 == 0) {
        DAT_ffff0000be1271a4 = 1;
        FUN_ffff0000be063b40(s_kedump__nand_device_ffff0000be0a7699);
        FUN_ffff0000be05fd98(s_kedump__nand_device_ffff0000be0a7699);
      }
      else {
        DAT_ffff0000be1271a4 = 2;
      }
      FUN_ffff0000be05def0(lVar3);
    }
  }
  else if (DAT_ffff0000be1271a4 == 1) {
    bVar1 = true;
  }
  else {
    bVar1 = false;
  }
  return bVar1;
}


