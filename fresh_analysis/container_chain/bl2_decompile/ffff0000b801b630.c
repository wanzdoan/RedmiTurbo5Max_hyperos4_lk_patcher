
undefined4 FUN_ffff0000b801b630(undefined8 param_1)

{
  int iVar1;
  int iVar2;
  long lVar3;
  undefined4 uVar4;
  undefined4 local_24;
  
  iVar1 = FUN_ffff0000b8047d28(param_1,s__pkvm_ffff0000b809f61e);
  local_24 = 0;
  lVar3 = FUN_ffff0000b804803c(param_1,iVar1,s_status_ffff0000b8096206,&local_24);
  if (((lVar3 == 0) || (iVar2 = FUN_ffff0000b804d248(lVar3,&DAT_ffff0000b808dcff), iVar2 != 0)) ||
     (iVar1 < 0)) {
    uVar4 = 0;
  }
  else {
    lVar3 = FUN_ffff0000b800c9f4
                      (0x200000,0x200000,0xffffffffffffffff,0,1,s_security_pkvm_mkp_ffff0000b808de3b
                      );
    uVar4 = 0xfffffffb;
    if (lVar3 != 0) {
      uVar4 = 0;
    }
  }
  return uVar4;
}


