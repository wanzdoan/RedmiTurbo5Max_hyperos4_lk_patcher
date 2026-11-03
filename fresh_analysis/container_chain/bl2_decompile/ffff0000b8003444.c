
byte FUN_ffff0000b8003444(void)

{
  char cVar1;
  int iVar2;
  byte bVar3;
  byte abStack_14 [4];
  
  iVar2 = FUN_ffff0000b8003458();
  if (iVar2 < 1) {
    DAT_ffff0000b810c010 = FUN_ffff0000b80263f0(1,5);
    if (DAT_ffff0000b810c010 == 0) {
      FUN_ffff0000b804c33c
                (s__s__get_spmi_device_fail_ffff0000b80897f3,
                 s_configure_board_id_pin_ffff0000b8085e5c);
      bVar3 = 0;
    }
    else {
      abStack_14[0] = 0;
      iVar2 = FUN_ffff0000b80262bc(DAT_ffff0000b810c010,0xb6,abStack_14,1);
      if (iVar2 < 0) {
        FUN_ffff0000b804c33c
                  (s__s__fail__addr___0x_x__ret____d_ffff0000b8093fab,
                   s_configure_board_id_pin_ffff0000b8085e5c,0xb6,iVar2);
        bVar3 = 0;
      }
      else {
        bVar3 = abStack_14[0] >> 6 & 1;
      }
    }
  }
  else {
    cVar1 = FUN_ffff0000b8003464();
    bVar3 = cVar1 - 1;
  }
  return bVar3;
}


