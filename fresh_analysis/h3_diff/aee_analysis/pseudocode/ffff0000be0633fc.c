
int FUN_ffff0000be0633fc(void)

{
  undefined8 uVar1;
  long lVar2;
  int iVar3;
  
  lVar2 = FUN_ffff0000be05df6c(s_expdb_ffff0000be0a79cf);
  if (lVar2 == 0) {
    FUN_ffff0000be063b40
              (s_kedump__Partition___s__is_not_ex_ffff0000be0a68a4,s_expdb_ffff0000be0a79cf);
    FUN_ffff0000be05fd98
              (s_kedump__Partition___s__is_not_ex_ffff0000be0a68a4,s_expdb_ffff0000be0a79cf);
  }
  else {
    iVar3 = -0x600000;
    if (DAT_ffff0000be1271a8 == '\0') {
      iVar3 = -0x300000;
    }
    iVar3 = iVar3 + *(int *)(lVar2 + 0x20);
    FUN_ffff0000be05def0();
    if (-1 < iVar3) {
      return iVar3;
    }
    uVar1 = 0x600000;
    if (DAT_ffff0000be1271a8 == '\0') {
      uVar1 = 0x300000;
    }
    FUN_ffff0000be063b40
              (s_kedump__partition_size__llx__is_l_ffff0000be0c6850,*(undefined8 *)(lVar2 + 0x20),
               uVar1);
    uVar1 = 0x600000;
    if (DAT_ffff0000be1271a8 == '\0') {
      uVar1 = 0x300000;
    }
    FUN_ffff0000be05fd98
              (s_kedump__partition_size__llx__is_l_ffff0000be0c6850,*(undefined8 *)(lVar2 + 0x20),
               uVar1);
  }
  return 0;
}


