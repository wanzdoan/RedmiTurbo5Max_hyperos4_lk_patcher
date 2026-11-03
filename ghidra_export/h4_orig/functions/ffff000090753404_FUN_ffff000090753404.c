// ffff000090753404  size=928  FUN_ffff000090753404

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090753404(int param_1,long param_2)

{
  code *pcVar1;
  code *pcVar2;
  undefined1 *puVar3;
  undefined1 auStack_780 [12];
  int local_774;
  int local_5e8;
  
  if (param_2 == 0) {
    return 0xffffffff;
  }
  if (param_1 == 0x2d) {
    puVar3 = &DAT_ffff000090879608;
    _DAT_ffff000090879608 = FUN_ffff0000907537a4;
    _DAT_ffff000090879628 = thunk_FUN_ffff00009077cea0;
    _DAT_ffff000090879630 = FUN_ffff0000907533f0;
    _DAT_ffff000090879618 = FUN_ffff000090753818;
    FUN_ffff00009077ee50(auStack_780,0,0x750);
    (**(code **)(param_2 + 0x18))(auStack_780);
    if (local_774 == 1) {
      pcVar1 = FUN_ffff000090753294;
      pcVar2 = FUN_ffff000090753150;
      _DAT_ffff000090879698 = FUN_ffff000090753240;
      _DAT_ffff000090879658 = FUN_ffff00009075318c;
      _DAT_ffff000090879660 = FUN_ffff000090753064;
      _DAT_ffff000090879668 = FUN_ffff0000907531ec;
    }
    else if (local_774 == 2) {
      pcVar1 = FUN_ffff0000907532b0;
      pcVar2 = FUN_ffff000090753164;
      _DAT_ffff000090879698 = FUN_ffff00009075325c;
      _DAT_ffff000090879658 = FUN_ffff0000907531ac;
      _DAT_ffff000090879660 = FUN_ffff000090753088;
      _DAT_ffff000090879668 = FUN_ffff000090753208;
    }
    else {
      pcVar1 = FUN_ffff0000907532cc;
      pcVar2 = FUN_ffff000090753178;
      _DAT_ffff000090879698 = FUN_ffff000090753278;
      _DAT_ffff000090879658 = FUN_ffff0000907531cc;
      _DAT_ffff000090879660 = FUN_ffff0000907530e4;
      _DAT_ffff000090879668 = FUN_ffff000090753224;
    }
    goto LAB_ffff0000907536dc;
  }
  if (param_1 == 0x2c) {
    puVar3 = &DAT_ffff000090879510;
    _DAT_ffff000090879510 = FUN_ffff0000907537a4;
    _DAT_ffff000090879530 = thunk_FUN_ffff00009077cea0;
    _DAT_ffff000090879538 = FUN_ffff0000907533f0;
    _DAT_ffff000090879520 = FUN_ffff000090753818;
    _DAT_ffff000090879560 = FUN_ffff0000907531ac;
    _DAT_ffff000090879568 = FUN_ffff000090753088;
    _DAT_ffff000090879570 = FUN_ffff000090753208;
    _DAT_ffff0000908795a0 = FUN_ffff00009075325c;
    _DAT_ffff0000908795d8 = FUN_ffff0000907530c8;
    _DAT_ffff0000908795e0 = FUN_ffff00009075312c;
    _DAT_ffff0000908795f8 = FUN_ffff000090753164;
    _DAT_ffff000090879600 = FUN_ffff0000907532b0;
    goto code_r0xffff0000907536e4;
  }
  if (param_1 != 0x2b) {
    return 0xffffffff;
  }
  puVar3 = &DAT_ffff000090879418;
  _DAT_ffff000090879418 = FUN_ffff0000907537a4;
  _DAT_ffff000090879438 = thunk_FUN_ffff00009077cea0;
  _DAT_ffff000090879440 = FUN_ffff0000907533f0;
  _DAT_ffff000090879428 = FUN_ffff000090753818;
  FUN_ffff00009077ee50(auStack_780,0,0x750);
  (**(code **)(param_2 + 0x18))(auStack_780);
  if (local_5e8 == 2) {
    if (local_774 == 2) {
      _DAT_ffff000090879470 = FUN_ffff000090753088;
      _DAT_ffff000090879478 = FUN_ffff000090753208;
      _DAT_ffff000090879468 = FUN_ffff0000907531ac;
      _DAT_ffff0000908794a8 = FUN_ffff00009075325c;
      pcVar1 = FUN_ffff0000907532b0;
      pcVar2 = FUN_ffff000090753164;
    }
    else {
      if (local_774 == 1) goto code_r0xffff00009075365c;
      _DAT_ffff000090879470 = FUN_ffff0000907530e4;
      _DAT_ffff000090879478 = FUN_ffff000090753224;
      _DAT_ffff000090879468 = FUN_ffff0000907531cc;
      _DAT_ffff0000908794a8 = FUN_ffff000090753240;
      pcVar1 = FUN_ffff000090753294;
      pcVar2 = FUN_ffff000090753178;
      puVar3 = &DAT_ffff000090879418;
    }
  }
  else {
    _DAT_ffff0000908794e0 = FUN_ffff0000907530ac;
    _DAT_ffff0000908794e8 = FUN_ffff000090753108;
code_r0xffff00009075365c:
    _DAT_ffff0000908794a8 = FUN_ffff000090753240;
    _DAT_ffff000090879478 = FUN_ffff0000907531ec;
    _DAT_ffff000090879470 = FUN_ffff000090753064;
    _DAT_ffff000090879468 = FUN_ffff00009075318c;
    pcVar1 = FUN_ffff000090753294;
    pcVar2 = FUN_ffff000090753150;
    puVar3 = &DAT_ffff000090879418;
  }
LAB_ffff0000907536dc:
  *(code **)(puVar3 + 0xe8) = pcVar2;
  *(code **)(puVar3 + 0xf0) = pcVar1;
code_r0xffff0000907536e4:
  *(code **)(puVar3 + 0xb8) = FUN_ffff0000907532e8;
  (**(code **)(param_2 + 8))(puVar3);
  return 0;
}


