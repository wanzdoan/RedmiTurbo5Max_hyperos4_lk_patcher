// ffff000090703458  size=136  FUN_ffff000090703458

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

byte FUN_ffff000090703458(int param_1)

{
  char cVar1;
  int iVar2;
  long lVar3;
  byte bVar4;
  long unaff_x29;
  
  if (param_1 < 1) {
    lVar3 = FUN_ffff000090740378(1,5);
    _DAT_ffff00009083d010 = lVar3;
    if (lVar3 == 0) {
      FUN_ffff00009077de54
                (s__s__get_spmi_device_fail_ffff0000907d7160,
                 s_configure_board_id_pin_ffff0000907d086d);
      bVar4 = 0;
    }
    else {
      *(undefined1 *)(unaff_x29 + -4) = 0;
      iVar2 = FUN_ffff0000907401d4(lVar3,0xb6,unaff_x29 + -4,1);
      if (iVar2 < 0) {
        FUN_ffff00009077de54
                  (s__s__fail__addr___0x_x__ret____d_ffff0000907ea3b9,
                   s_configure_board_id_pin_ffff0000907d086d,0xb6,iVar2);
        bVar4 = 0;
      }
      else {
        bVar4 = *(byte *)(unaff_x29 + -4) >> 6 & 1;
      }
    }
  }
  else {
    cVar1 = FUN_ffff000090703464();
    bVar4 = cVar1 - 1;
  }
  return bVar4;
}


