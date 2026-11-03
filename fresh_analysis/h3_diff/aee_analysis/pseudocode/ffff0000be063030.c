
bool FUN_ffff0000be063030(void)

{
  int *piVar1;
  long lVar2;
  long lVar3;
  bool bVar4;
  
  piVar1 = (int *)FUN_ffff0000be05f1bc(0x2030);
  if (piVar1 == (int *)0x0) {
    FUN_ffff0000be063b40
              (s_kedump__malloc_failed_at__s_ffff0000be0c5f85,s_is_expdb_valid_ffff0000be0c4807);
    FUN_ffff0000be05fd98
              (s_kedump__malloc_failed_at__s_ffff0000be0c5f85,s_is_expdb_valid_ffff0000be0c4807);
    return false;
  }
  FUN_ffff0000be060ae0(piVar1,0,0x2030);
  lVar2 = FUN_ffff0000be05df6c(s_expdb_ffff0000be0a79cf);
  if (lVar2 == 0) {
    FUN_ffff0000be063b40
              (s_kedump__Partition___s__is_not_ex_ffff0000be0a68a4,s_expdb_ffff0000be0a79cf);
    FUN_ffff0000be05fd98
              (s_kedump__Partition___s__is_not_ex_ffff0000be0a68a4,s_expdb_ffff0000be0a79cf);
    thunk_FUN_ffff0000be09937c(piVar1);
    return false;
  }
  lVar3 = FUN_ffff0000be05e000(lVar2,piVar1,0,0x2030);
  if (lVar3 < 0x2030) {
    FUN_ffff0000be063b40(s_kedump__WARN___read_size_from_ex_ffff0000be0c4816,lVar3);
    FUN_ffff0000be05fd98(s_kedump__WARN___read_size_from_ex_ffff0000be0c4816,lVar3);
  }
  else if (*piVar1 == -0x511f2153) {
    bVar4 = 0x11 < (uint)piVar1[1];
    goto LAB_ffff0000be063148;
  }
  bVar4 = false;
LAB_ffff0000be063148:
  thunk_FUN_ffff0000be09937c(piVar1);
  FUN_ffff0000be05def0(lVar2);
  return bVar4;
}


