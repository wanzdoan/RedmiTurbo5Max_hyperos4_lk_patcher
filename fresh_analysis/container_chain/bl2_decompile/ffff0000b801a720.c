
void FUN_ffff0000b801a720(void)

{
  int iVar1;
  
  iVar1 = FUN_ffff0000b8020220();
  if (iVar1 != 0) {
    FUN_ffff0000b804c33c
              (s__s__d__Detected_just_recover_fro_ffff0000b808b7c4,
               s_detect_pmic_just_rst_ffff0000b8086239,0xf8);
    FUN_ffff0000b802027c();
  }
  DAT_ffff0000b811b250 = iVar1 != 0;
  return;
}


