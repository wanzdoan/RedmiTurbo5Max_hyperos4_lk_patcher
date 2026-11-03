
void FUN_ffff0000b8005974(void)

{
  int iVar1;
  char *pcVar2;
  ulong uVar3;
  undefined8 uVar4;
  undefined *puVar5;
  ulong uVar6;
  
  DAT_ffff0000b810c078 = FUN_ffff0000b8020e20(s_mt6379_ffff0000b8094000);
  if (DAT_ffff0000b810c078 == 0) {
    pcVar2 = s__s__d__Failed_to_get_mt6379_regm_ffff0000b80950d7;
    uVar4 = 0x151;
  }
  else {
    iVar1 = FUN_ffff0000b8027550();
    if (iVar1 != 0) {
      uVar6 = 0;
      puVar5 = &DAT_ffff0000b80ad890;
      do {
        iVar1 = FUN_ffff0000b8020df8(puVar5);
        if (iVar1 == 0) {
          pcVar2 = s__s__d__Register__s_dlpt_device_s_ffff0000b808e7f4;
          uVar4 = 0x160;
        }
        else {
          uVar4 = 0x15e;
          pcVar2 = s__s__d__Failed_to_register__s_dlp_ffff0000b809b3a0;
        }
        FUN_ffff0000b804c33c
                  (pcVar2,s_mt6379_dlpt_init_ffff0000b80950fb,uVar4,
                   (&PTR_s_mt6379_bat1_ffff0000b80ad8c0)[uVar6]);
        uVar6 = uVar6 + 1;
        uVar3 = FUN_ffff0000b8027550();
        puVar5 = puVar5 + 0x18;
      } while (uVar6 < (uVar3 & 0xffffffff));
    }
    pcVar2 = s__s__d__LK_init_done__ffff0000b80997d2;
    uVar4 = 0x163;
  }
  FUN_ffff0000b804c33c(pcVar2,s_mt6379_dlpt_init_ffff0000b80950fb,uVar4);
  return;
}


