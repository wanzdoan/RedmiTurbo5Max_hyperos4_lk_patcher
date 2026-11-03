// ffff0000907ce1dc  size=204  FUN_ffff0000907ce1dc

bool FUN_ffff0000907ce1dc(uint param_1)

{
  undefined4 uVar1;
  int iVar2;
  long lVar3;
  undefined *puVar4;
  
  lVar3 = FUN_ffff000090741944(s_usb2jtag_ffff0000907f59f4);
  if (lVar3 == 0) {
    uVar1 = 0;
  }
  else {
    FUN_ffff000090741944(s_usb2jtag_ffff0000907f59f4);
    uVar1 = FUN_ffff00009077d464();
  }
  FUN_ffff00009077de54
            (s__s__d___USB2JTAG__current_settin_ffff0000907d710b,s_set_usb2jtag_ffff0000907fc86c,
             0x26,uVar1);
  puVar4 = (&PTR_DAT_ffff000090838aa0)[param_1];
  iVar2 = FUN_ffff000090741a20(s_usb2jtag_ffff0000907f59f4,puVar4);
  if (iVar2 == 0) {
    FUN_ffff00009077de54
              (s__s__d___USB2JTAG_set_USB2JTAG__s_ffff0000907d7135,s_set_usb2jtag_ffff0000907fc86c,
               0x28,puVar4);
  }
  else {
    FUN_ffff00009077de54
              (s__s__d___USB2JTAG_set_USB2JTAG__s_ffff0000907d3caa,s_set_usb2jtag_ffff0000907fc86c,
               0x2a,puVar4);
  }
  return iVar2 != 0;
}


