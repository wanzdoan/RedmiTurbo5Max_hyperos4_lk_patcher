
byte FUN_ffff0000be003444(void)

{
  char cVar1;
  int iVar2;
  byte bVar3;
  byte abStack_14 [4];
  
  iVar2 = FUN_ffff0000be003458();
  if (iVar2 < 1) {
    DAT_ffff0000be0f9010 = FUN_ffff0000be0303e8(1,5);
    if (DAT_ffff0000be0f9010 == 0) {
      FUN_ffff0000be05fd98
                (s__s__get_spmi_device_fail_ffff0000be0a8b7b,
                 s_configure_board_id_pin_ffff0000be0a40a8);
      bVar3 = 0;
    }
    else {
      abStack_14[0] = 0;
      iVar2 = FUN_ffff0000be0302b4(DAT_ffff0000be0f9010,0xb6,abStack_14,1);
      if (iVar2 < 0) {
        FUN_ffff0000be05fd98
                  (s__s__fail__addr___0x_x__ret____d_ffff0000be0b5f4d,
                   s_configure_board_id_pin_ffff0000be0a40a8,0xb6,iVar2);
        bVar3 = 0;
      }
      else {
        bVar3 = abStack_14[0] >> 6 & 1;
      }
    }
  }
  else {
    cVar1 = FUN_ffff0000be003464();
    bVar3 = cVar1 - 1;
  }
  return bVar3;
}


