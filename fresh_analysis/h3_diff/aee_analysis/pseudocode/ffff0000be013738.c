
void FUN_ffff0000be013738(long param_1)

{
  long lVar1;
  undefined8 uVar2;
  code *pcVar3;
  
  if ((param_1 != 0) && (DAT_ffff0000be107948 == 1)) {
    lVar1 = FUN_ffff0000be05f204(0x40,0x40);
    if (lVar1 != 0) {
      FUN_ffff0000be0611dc(lVar1,0x40,s_INFO_s_ffff0000be0c6057,param_1);
      pcVar3 = *(code **)(DAT_ffff0000be107960 + 0x30);
      uVar2 = FUN_ffff0000be060e5c(lVar1);
      (*pcVar3)(lVar1,uVar2,5000);
      thunk_FUN_ffff0000be09937c(lVar1);
      return;
    }
    FUN_ffff0000be05fd98(s_fastboot__can_t_allocate_memory_ffff0000be0c4ee7);
    return;
  }
  return;
}


