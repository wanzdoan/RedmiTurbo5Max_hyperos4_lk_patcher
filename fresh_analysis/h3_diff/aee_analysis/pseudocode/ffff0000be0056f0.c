
void FUN_ffff0000be0056f0(void)

{
  int iVar1;
  char *pcVar2;
  ulong uVar3;
  undefined8 uVar4;
  undefined *puVar5;
  ulong uVar6;
  
  DAT_ffff0000be0f9058 = FUN_ffff0000be02956c(s_mt6379_ffff0000be0b5f6e);
  if (DAT_ffff0000be0f9058 == 0) {
    pcVar2 = s__s__d__Failed_to_get_mt6379_regm_ffff0000be0b725a;
    uVar4 = 0x151;
  }
  else {
    iVar1 = FUN_ffff0000be031548();
    if (iVar1 != 0) {
      uVar6 = 0;
      puVar5 = &DAT_ffff0000be0d58e0;
      do {
        iVar1 = FUN_ffff0000be029544(puVar5);
        if (iVar1 == 0) {
          pcVar2 = s__s__d__Register__s_dlpt_device_s_ffff0000be0af33c;
          uVar4 = 0x160;
        }
        else {
          uVar4 = 0x15e;
          pcVar2 = s__s__d__Failed_to_register__s_dlp_ffff0000be0bf109;
        }
        FUN_ffff0000be05fd98
                  (pcVar2,s_mt6379_dlpt_init_ffff0000be0b727e,uVar4,
                   (&PTR_s_mt6379_bat1_ffff0000be0d5910)[uVar6]);
        uVar6 = uVar6 + 1;
        uVar3 = FUN_ffff0000be031548();
        puVar5 = puVar5 + 0x18;
      } while (uVar6 < (uVar3 & 0xffffffff));
    }
    pcVar2 = s__s__d__LK_init_done__ffff0000be0bcb75;
    uVar4 = 0x163;
  }
  FUN_ffff0000be05fd98(pcVar2,s_mt6379_dlpt_init_ffff0000be0b727e,uVar4);
  return;
}


