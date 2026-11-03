
byte FUN_ffff000090703444(void)

{
  char cVar1;
  int iVar2;
  byte bVar3;
  byte abStack_14 [4];
  
  iVar2 = FUN_ffff000090703458();
  if (iVar2 < 1) {
    DAT_ffff00009083d010 = FUN_ffff000090740610(1,5);
    if (DAT_ffff00009083d010 == 0) {
      FUN_ffff00009077e11c
                (s__s__get_spmi_device_fail_ffff0000907d71f6,
                 s_configure_board_id_pin_ffff0000907d088c);
      bVar3 = 0;
    }
    else {
      abStack_14[0] = 0;
      iVar2 = FUN_ffff00009074046c(DAT_ffff00009083d010,0xb6,abStack_14,1);
      if (iVar2 < 0) {
        FUN_ffff00009077e11c
                  (s__s__fail__addr___0x_x__ret____d_ffff0000907ea514,
                   s_configure_board_id_pin_ffff0000907d088c,0xb6,iVar2);
        bVar3 = 0;
      }
      else {
        bVar3 = abStack_14[0] >> 6 & 1;
      }
    }
  }
  else {
    cVar1 = FUN_ffff000090703464();
    bVar3 = cVar1 - 1;
  }
  return bVar3;
}


