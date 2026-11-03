
void FUN_ffff0000be013660(undefined8 param_1,undefined *param_2)

{
  undefined *puVar1;
  long lVar2;
  undefined8 uVar3;
  code *pcVar4;
  
  if (DAT_ffff0000be107948 != 1) {
    return;
  }
  lVar2 = FUN_ffff0000be05f204(0x40,0x40);
  if (lVar2 != 0) {
    puVar1 = &DAT_ffff0000be0ae340;
    if (param_2 != (undefined *)0x0) {
      puVar1 = param_2;
    }
    FUN_ffff0000be0611dc(lVar2,0x40,&DAT_ffff0000be0af57f,param_1,puVar1);
    DAT_ffff0000be107948 = 2;
    pcVar4 = *(code **)(DAT_ffff0000be107960 + 0x30);
    uVar3 = FUN_ffff0000be060e5c(lVar2);
    (*pcVar4)(lVar2,uVar3,5000);
    thunk_FUN_ffff0000be09937c(lVar2);
    return;
  }
  FUN_ffff0000be05fd98(s_fastboot__can_t_allocate_memory_ffff0000be0c4ee7);
  return;
}


